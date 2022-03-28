#!/bin/bash

oneTimeSetUp() {
	export FABRIC_CFG_PATH=$PWD/../config/

	if [[ -z "$GITHUB_WORKSPACE" ]]; then
		GITHUB_WORKSPACE=~/atm-hyperledger/
	fi

	source $GITHUB_WORKSPACE/src/test/shell/as-org1.sh

	rm -f $GITHUB_WORKSPACE/atm.tar.gz
	rm -rf "$GITHUB_WORKSPACE/build/install/"
	pushd "$GITHUB_WORKSPACE"
	./gradlew installDist
	popd
	peer lifecycle chaincode package $GITHUB_WORKSPACE/atm.tar.gz --path $GITHUB_WORKSPACE/build/install/atm --lang java --label atm_1.0

}

setUp() {
	./network.sh down
	./network.sh up createChannel

	# start a subshell due to export variables.
	(
		export CORE_PEER_TLS_ENABLED=true
		export CORE_PEER_LOCALMSPID="Org1MSP"
		export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
		export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
		export CORE_PEER_ADDRESS=localhost:7051
		# The package ID is the combination of the chaincode label and a hash of the chaincode binaries. Every peer will generate the same package ID.
		packageId=$(peer lifecycle chaincode install $GITHUB_WORKSPACE/atm.tar.gz 2>&1 | grep -o -P '(?<=identifier:\s).+:[\da-f]+$')
		if [[ -z "$packageId" ]]; then
			fail "Failed to install chaincode."
			return
		fi
		peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name atm --version 1.0 --package-id $packageId --sequence 1 --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem

		export CORE_PEER_LOCALMSPID="Org2MSP"
		export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
		export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
		export CORE_PEER_ADDRESS=localhost:9051
		peer lifecycle chaincode install $GITHUB_WORKSPACE/atm.tar.gz

		peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name atm --version 1.0 --package-id $packageId --sequence 1 --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem

		peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name atm --version 1.0 --sequence 1 --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem --peerAddresses localhost:7051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
	)
}

getBlockInfo() {
	peer channel fetch newest mychannel.block -c mychannel -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
	configtxlator proto_decode --type common.Block --input mychannel.block
}

testManageUser() {
	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageUserCRUDServiceImpl:createUser","Args":["1","Tom","Los Angeles"]}'

	docker stop "$(docker ps -n 1 --filter 'name=dev' --format '{{.ID}}')"

	output=$(peer chaincode query -C mychannel -n atm -c '{"function":"ManageUserCRUDServiceImpl:queryUser","Args":["1"]}')
	assertContains "$output" "Tom"
	assertContains "$output" "Los Angeles"

	if pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageUserCRUDServiceImpl:modifyUser","Args":["2","James","San Diego"]}'; then
		fail || return
	fi

	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageUserCRUDServiceImpl:modifyUser","Args":["1","Jessica","San Francisco"]}'

	docker stop "$(docker ps -n 1 --filter 'name=dev' --format '{{.ID}}')"

	output=$(peer chaincode query -C mychannel -n atm -c '{"function":"ManageUserCRUDServiceImpl:queryUser","Args":["1"]}')
	assertContains "$output" "Jessica"
	assertContains "$output" "Francisco"

	if pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageUserCRUDServiceImpl:deleteUser","Args":["2"]}'; then
		fail || return
	fi

	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageUserCRUDServiceImpl:deleteUser","Args":["1"]}'

	if pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageUserCRUDServiceImpl:deleteUser","Args":["1"]}'; then
		fail "cannot delete the same user again" || return
	fi
}

testmanageCard() {
	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageBankCardCRUDServiceImpl:createBankCard","Args":["1","NORMAL","CREDIT","666","10"]}'

	docker stop "$(docker ps -n 1 --filter 'name=dev' --format '{{.ID}}')"
	
	if pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageBankCardCRUDServiceImpl:createBankCard","Args":["1","NORMAL","CREDIT","666","10"]}'; then
		fail || return
	fi
	docker stop "$(docker ps -n 1 --filter 'name=dev' --format '{{.ID}}')"

	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageBankCardCRUDServiceImpl:queryBankCard","Args":["1"]}'

	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageBankCardCRUDServiceImpl:modifyBankCard","Args":["1", "CANNEL", "DESPOSIT", "888","100"]}'
	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageBankCardCRUDServiceImpl:modifyBankCard","Args":["1", "CANNEL", "DESPOSIT", "888","100"]}'

	pci -C mychannel -n atm --waitForEvent -c '{"function":"ManageBankCardCRUDServiceImpl:deleteBankCard","Args":["1"]}'
}

source shunit2
