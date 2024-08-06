package aChaushev.architects.web;

import aChaushev.architects.model.enums.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConversionServiceTest {
    @Autowired
    private ConversionService conversionService;

    @Test
    public void testStringToUserRoleEnumConverter() {
        UserRoleEnum role = conversionService.convert("ADMIN", UserRoleEnum.class);
        assertEquals(UserRoleEnum.ADMIN, role);
    }
}

