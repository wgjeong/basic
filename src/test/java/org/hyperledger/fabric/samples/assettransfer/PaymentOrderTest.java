/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public final class PaymentOrderTest {

    @Nested
    class Equality {

        @Test
        public void isReflexive() {
            PaymentOrder paymentOrder = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");

            assertThat(paymentOrder).isEqualTo(paymentOrder);
        }

        @Test
        public void isSymmetric() {
            PaymentOrder paymentOrderA = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");
            PaymentOrder paymentOrderB = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");

            assertThat(paymentOrderA).isEqualTo(paymentOrderB);
            assertThat(paymentOrderB).isEqualTo(paymentOrderA);
        }

        @Test
        public void isTransitive() {
            PaymentOrder paymentOrderA = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");
            PaymentOrder paymentOrderB = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");
            PaymentOrder paymentOrderC = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");

            assertThat(paymentOrderA).isEqualTo(paymentOrderB);
            assertThat(paymentOrderB).isEqualTo(paymentOrderC);
            assertThat(paymentOrderA).isEqualTo(paymentOrderC);
        }

        @Test
        public void handlesInequality() {
            PaymentOrder paymentOrderA = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");
            PaymentOrder paymentOrderB = new PaymentOrder("paymentOrder2", "20210121", "100000", "KRW", "테스트2", "미국어딘가", "orderer2", "101010101010", "receiver2", "한국어딘가", "원화를 보낸다", "편하지?", "2");

            assertThat(paymentOrderA).isNotEqualTo(paymentOrderB);
        }

        @Test
        public void handlesOtherObjects() {
            PaymentOrder paymentOrderA = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");
            String paymentOrderB = "not a paymentOrder";

            assertThat(paymentOrderA).isNotEqualTo(paymentOrderB);
        }

        @Test
        public void handlesNull() {
            PaymentOrder paymentOrder = new PaymentOrder("paymentOrder1", "20210121", "70000", "KRW", "테스트1", "서울어딘가", "orderer1", "010101010101", "receiver1", "미국어딘가", "원화를 보낸다", "재주껏 바꿔써라", "1");

            assertThat(paymentOrder).isNotEqualTo(null);
        }
    }
}
