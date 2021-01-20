/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import com.owlike.genson.Genson;

@Contract(
        name = "basic",
        info = @Info(
                title = "Swift Blockchain",
                description = "Instead of the Swift System",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "dobby@woorifis.com",
                        name = "DF_BC_1",
                        url = "https://github.com/wgjeong/swiftbc")))
@Default
public final class SwiftBc implements ContractInterface {

    private final Genson genson = new Genson();

    private enum SwiftBcErrors {
        NOT_ENOUGH_PARAMETER,
        PAYMENTORDER_NOT_FOUND,
        PAYMENTORDER_ALREADY_EXISTS
    }

    /**
     * Creates some initial assets on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        CreatePaymentOrder(ctx, "paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");
        CreatePaymentOrder(ctx, "paymentOrder2", "20210121", "100000", "KRW", "테스트2", "미국어딘가", "orderer2", "101010101010", "receiver2", "한국어딘가", "원화를 보낸다", "편하지?", "2");
    }

    /**
     * Creates a new asset on the ledger.
     *
     * @param ctx the transaction context
     * @param swiftCode 참조번호
     * @param date 일자
     * @param amount 금액
     * @param currency 통화
     * @param ordererName 송금인 명
     * @param ordererAddress 송금인 주소
     * @param ordererId 송금인 고객번호
     * @param receiverAccount 수취인 계좌번호
     * @param receiverName 수취인 명
     * @param receiverAddress 수취인 주소
     * @param remittanceInformation 송금 목적
     * @param additionalInstruction 추가 지시사항
     * @param chargeDetail 수수료 징수구분(부담자 구분)
     * @return the created paymentOrder
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public PaymentOrder CreatePaymentOrder(final Context ctx, final String  swiftCode, final String  date, final String  amount, final String  currency, final String  ordererName, final String  ordererAddress, final String  ordererId, final String  receiverAccount, final String  receiverName, final String  receiverAddress, final String  remittanceInformation, final String  additionalInstruction, final String  chargeDetail) {
        ChaincodeStub stub = ctx.getStub();

        validationParam(swiftCode, date, amount, currency, ordererName, ordererAddress, ordererId, receiverAccount, receiverName, receiverAddress, remittanceInformation, chargeDetail);

        if (PaymentOrderExists(ctx, swiftCode)) {
            String errorMessage = "The Payment Order (" + swiftCode + ") already exists";
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SwiftBcErrors.PAYMENTORDER_ALREADY_EXISTS.toString());
        }

        PaymentOrder paymentOrder = new PaymentOrder(swiftCode, date, amount, currency, ordererName, ordererAddress, ordererId, receiverAccount, receiverName, receiverAddress, remittanceInformation, additionalInstruction, chargeDetail);
        String paymentOrderJSON = genson.serialize(paymentOrder);
        stub.putStringState(swiftCode, paymentOrderJSON);

        return paymentOrder;
    }

    private boolean validationParam(final String swiftCode, final String date, final String amount, final String currency, final String ordererName, final String ordererAddress, final String ordererId, final String receiverAccount, final String receiverName, final String receiverAddress, final String remittanceInformation, final String chargeDetail) {
        isEmpty("swiftCode", swiftCode);
        isEmpty("date", date);
        isEmpty("amount", amount);
        isEmpty("currency", currency);
        isEmpty("ordererName", ordererName);
        isEmpty("ordererAddress", ordererAddress);
        isEmpty("ordererId", ordererId);
        isEmpty("receiverName", receiverName);
        isEmpty("receiverAccount", receiverAccount);
        isEmpty("receiverAddress", receiverAddress);
        isEmpty("remittanceInformation", remittanceInformation);
        isEmpty("chargeDetail", chargeDetail);
        return true;
    }

    private void isEmpty(final String paramName, final String param) {
        if (null == param || "".equals(param) || ("amount".equals(paramName) && "0".equals(param))) {
            throw new ChaincodeException(paramName + " is empty", SwiftBcErrors.NOT_ENOUGH_PARAMETER.toString());
        }
    }

    /**
     * Retrieves an paymentOrder with the specified ID from the ledger.
     *
     * @param ctx the transaction context
     * @param swiftCode 참조번호
     * @return the paymentOrder found on the ledger if there was one
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public PaymentOrder ReadPaymentOrder(final Context ctx, final String swiftCode) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(swiftCode);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = "The Payment Order (" + swiftCode + ") does not exist";
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SwiftBcErrors.PAYMENTORDER_NOT_FOUND.toString());
        }
        PaymentOrder paymentOrder = genson.deserialize(assetJSON, PaymentOrder.class);
        return paymentOrder;
    }

    /**
     * Checks the existence of the paymentOrder on the ledger
     *
     * @param ctx the transaction context
     * @param swiftCode 참조번호
     * @return boolean indicating the existence of the paymentOrder
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean PaymentOrderExists(final Context ctx, final String swiftCode) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(swiftCode);

        return (assetJSON != null && !assetJSON.isEmpty());
    }
}
