package io.redispro.redisexec;

import io.redispro.redisexec.utils.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordUtilTest {

    @Test
    void testGetEncodedPw_WithRealEncoder() {
        // Given
        String rawPassword = "myPassword123";

        // When
        String encodedPassword = PasswordUtil.getEncodedPw(rawPassword);

        // Then
        // JUnit에서 제공하는 단언문(Assertion) 중 하나로, 특정 객체가 null이 아닌지 확인하는 데 사용됩니다.
        // assertNotNull을 사용하여 메서드나 연산 결과가 유효한 값을 반환했는지 확인할 수 있습니다. 객체가 null이면 테스트가 실패하고, null이 아니라면 테스트가 성공
        assertNotNull(encodedPassword, "Encoded password should not be null");

        //JUnit에서 제공하는 단언문(Assertion)으로, 두 값이 서로 다른지 확인할 때 사용됩니다.
        //만약 두 값이 같으면 테스트가 실패하고, 다르면 테스트가 성공
        assertNotEquals(rawPassword, encodedPassword, "Encoded password should not match the raw password");
    }

}
