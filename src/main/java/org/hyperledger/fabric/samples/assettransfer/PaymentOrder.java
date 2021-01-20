/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class PaymentOrder {

    @Property()
    private String swiftCode;               // 참조번호
    @Property()
    private String date;                    // 일자
    @Property()
    private String amount;                  // 금액
    @Property()
    private String currency;                // 통화
    @Property()
    private String ordererName;             // 송금인 명
    @Property()
    private String ordererAddress;          // 송금인 주소
    @Property()
    private String ordererId;               // 송금인 고객번호
    @Property()
    private String receiverAccount;         // 수취인 계좌번호
    @Property()
    private String receiverName;            // 수취인 명
    @Property()
    private String receiverAddress;         // 수취인 주소
    @Property()
    private String remittanceInformation;   // 송금 목적
    @Property()
    private String additionalInstruction;   // 추가 지시사항
    @Property()
    private String chargeDetail;            // 수수료 징수구분(부담자 구분)

    public PaymentOrder( @JsonProperty("swiftCode"            ) final String swiftCode
                       , @JsonProperty("date"                 ) final String date
                       , @JsonProperty("amount"               ) final String amount
                       , @JsonProperty("currency"             ) final String currency
                       , @JsonProperty("ordererName"          ) final String ordererName
                       , @JsonProperty("ordererAddress"       ) final String ordererAddress
                       , @JsonProperty("ordererId"            ) final String ordererId
                       , @JsonProperty("receiverAccount"      ) final String receiverAccount
                       , @JsonProperty("receiverName"         ) final String receiverName
                       , @JsonProperty("receiverAddress"      ) final String receiverAddress
                       , @JsonProperty("remittanceInformation") final String remittanceInformation
                       , @JsonProperty("additionalInstruction") final String additionalInstruction
                       , @JsonProperty("chargeDetail"         ) final String chargeDetail ) {
        this.swiftCode = swiftCode;
        this.date = date;
        this.amount = amount;
        this.currency = currency;
        this.ordererName = ordererName;
        this.ordererAddress = ordererAddress;
        this.ordererId = ordererId;
        this.receiverAccount = receiverAccount;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.remittanceInformation = remittanceInformation;
        this.additionalInstruction = additionalInstruction;
        this.chargeDetail = chargeDetail;
    }

    public String getSwiftCode() {
        return swiftCode;
    }
    public String getDate() {
        return date;
    }
    public String getAmount() {
        return amount;
    }
    public String getCurrency() {
        return currency;
    }
    public String getOrdererName() {
        return ordererName;
    }
    public String getOrdererAddress() {
        return ordererAddress;
    }
    public String getOrdererId() {
        return ordererId;
    }
    public String getReceiverAccount() {
        return receiverAccount;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public String getReceiverAddress() {
        return receiverAddress;
    }
    public String getRemittanceInformation() {
        return remittanceInformation;
    }
    public String getAdditionalInstruction() {
        return additionalInstruction;
    }
    public String getChargeDetail() {
        return chargeDetail;
    }
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        PaymentOrder other = (PaymentOrder) obj;

        return Objects.deepEquals(
                new String[] {getSwiftCode()},
                new String[] {other.getSwiftCode()});
    }

    @Override
    public int hashCode() {
        return Objects.hash( getSwiftCode()
                           , getDate()
                           , getAmount()
                           , getCurrency()
                           , getOrdererName()
                           , getOrdererAddress()
                           , getOrdererId()
                           , getReceiverAccount()
                           , getReceiverName()
                           , getReceiverAddress()
                           , getRemittanceInformation()
                           , getAdditionalInstruction()
                           , getChargeDetail() );
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [swiftCode=" + swiftCode + ", currency=" + currency
                + ", ordererName=" + ordererName + ", ordererAddress=" + ordererAddress + ", ordererId=" + ordererId + ", receiverAccount=" + receiverAccount
                + ", receiverName=" + receiverName + ", receiverAddress=" + receiverAddress + ", remittanceInformation=" + remittanceInformation
                + ", additionalInstruction=" + additionalInstruction + ", chargeDetail=" + chargeDetail + "]";
    }
}