/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public final class SwiftBcTest {

    private final class MockKeyValue implements KeyValue {

        private final String key;
        private final String value;

        MockKeyValue(final String key, final String value) {
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getStringValue() {
            return this.value;
        }

        @Override
        public byte[] getValue() {
            return this.value.getBytes();
        }

    }

    private final class MockPaymentOrderResultsIterator implements QueryResultsIterator<KeyValue> {

        private final List<KeyValue> paymentOrderList;

        MockPaymentOrderResultsIterator() {
            super();

            paymentOrderList = new ArrayList<KeyValue>();

            paymentOrderList.add(new MockKeyValue("paymentOrder1",
                    "{ \"swiftCode\": \"paymentOrder1\", \"date\": \"20210121\", \"amount\": \"70000\" , \"currency\": \"KRW\", \"ordererName\": \"테스트1\", \"ordererAddress\": \"서울어딘가\", \"ordererId\": \"orderer1\", \"receiverAccount\": \"010101010101\", \"receiverName\": \"receiver1\", \"receiverAddress\": \"미국어딘가\", \"remittanceInformation\": \"원화를 보낸다\", \"additionalInstruction\": \"재주껏 바꿔써라\", \"chargeDetail\": \"1\" }"));
            paymentOrderList.add(new MockKeyValue("paymentOrder2",
                    "{ \"swiftCode\": \"paymentOrder2\", \"date\": \"20210121\", \"amount\": \"100000\" , \"currency\": \"KRW\", \"ordererName\": \"테스트2\", \"ordererAddress\": \"미국어딘가\", \"ordererId\": \"orderer2\", \"receiverAccount\": \"101010101010\", \"receiverName\": \"receiver2\", \"receiverAddress\": \"한국어딘가\", \"remittanceInformation\": \"원화를 보낸다\", \"additionalInstruction\": \"편하지?\", \"chargeDetail\": \"2\" }"));
        }

        @Override
        public Iterator<KeyValue> iterator() {
            return paymentOrderList.iterator();
        }

        @Override
        public void close() throws Exception {
            // do nothing
        }
    }

    @Test
    public void invokeUnknownTransaction() {
        SwiftBc contract = new SwiftBc();
        Context ctx = mock(Context.class);

        Throwable thrown = catchThrowable(() -> {
            contract.unknownTransaction(ctx);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Undefined contract method called");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);

        verifyZeroInteractions(ctx);
    }

    @Nested
    class InvokeReadPaymentOrderTransaction {

        @Test
        public void whenPaymentOrderExists() {
            SwiftBc contract = new SwiftBc();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("paymentOrder1"))
                    .thenReturn("{ \"swiftCode\": \"paymentOrder1\", \"date\": \"20210121\", \"amount\": \"70000\" , \"currency\": \"KRW\", \"ordererName\": \"테스트1\", \"ordererAddress\": \"서울어딘가\", \"ordererId\": \"orderer1\", \"receiverAccount\": \"010101010101\", \"receiverName\": \"receiver1\", \"receiverAddress\": \"미국어딘가\", \"remittanceInformation\": \"원화를 보낸다\", \"additionalInstruction\": \"재주껏 바꿔써라\", \"chargeDetail\": \"1\" }");

            PaymentOrder paymentOrder = contract.ReadPaymentOrder(ctx, "paymentOrder1");

            assertThat(paymentOrder).isEqualTo(new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1"));
        }

        @Test
        public void whenPaymentOrderDoesNotExist() {
            SwiftBc contract = new SwiftBc();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("paymentOrder1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.ReadPaymentOrder(ctx, "paymentOrder1");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("The Payment Order (paymentOrder1) does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("PAYMENTORDER_NOT_FOUND".getBytes());
        }
    }

    @Test
    void invokeInitLedgerTransaction() {
        SwiftBc contract = new SwiftBc();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        contract.InitLedger(ctx);

        InOrder inOrder = inOrder(stub);
        inOrder.verify(stub).putStringState("paymentOrder1",
                "{ \"swiftCode\": \"paymentOrder1\", \"date\": \"20210121\", \"amount\": \"70000\", \"currency\": \"KRW\", \"ordererName\": \"테스트1\", \"ordererAddress\": \"서울어딘가\", \"ordererId\": \"orderer1\", \"receiverAccount\": \"010101010101\", \"receiverName\": \"receiver1\", \"receiverAddress\": \"미국어딘가\", \"remittanceInformation\": \"원화를 보낸다\", \"additionalInstruction\": \"재주껏 바꿔써라\", \"chargeDetail\": \"1\" }");
        inOrder.verify(stub).putStringState("paymentOrder2",
                "{ \"swiftCode\": \"paymentOrder2\", \"date\": \"20210121\", \"amount\": \"100000\", \"currency\": \"KRW\", \"ordererName\": \"테스트2\", \"ordererAddress\": \"미국어딘가\", \"ordererId\": \"orderer2\", \"receiverAccount\": \"101010101010\", \"receiverName\": \"receiver2\", \"receiverAddress\": \"한국어딘가\", \"remittanceInformation\": \"원화를 보낸다\", \"additionalInstruction\": \"편하지?\", \"chargeDetail\": \"2\" }");
    }

    @Nested
    class InvokeCreatePaymentOrderTransaction {

        @Test
        public void whenPaymentOrderExists() {
            SwiftBc contract = new SwiftBc();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("paymentOrder1"))
                    .thenReturn("{ \"swiftCode\": \"paymentOrder1\", \"date\": \"20210121\", \"amount\": \"70000\" , \"currency\": \"KRW\", \"ordererName\": \"테스트1\", \"ordererAddress\": \"서울어딘가\", \"ordererId\": \"orderer1\", \"receiverAccount\": \"010101010101\", \"receiverName\": \"receiver1\", \"receiverAddress\": \"미국어딘가\", \"remittanceInformation\": \"원화를 보낸다\", \"additionalInstruction\": \"재주껏 바꿔써라\", \"chargeDetail\": \"1\" }");

            Throwable thrown = catchThrowable(() -> {
                contract.CreatePaymentOrder(ctx, "paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("The Payment Order (paymentOrder1) already exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("PAYMENTORDER_ALREADY_EXISTS".getBytes());
        }

        @Test
        public void whenPaymentOrderDoesNotExist() {
            SwiftBc contract = new SwiftBc();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("paymentOrder1")).thenReturn("");

            PaymentOrder paymentOrder = contract.CreatePaymentOrder(ctx, "paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");

            assertThat(paymentOrder).isEqualTo(new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1"));
        }
    }
}
