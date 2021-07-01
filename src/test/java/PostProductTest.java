import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static requests.UserEndpoint.*;

import static requests.ProductEndpoint.*;

public class PostProductTest extends TestBase {

    private User user, notAdminUser, userNotAuthenticated;
    private Product product, product2, fixedProduct;

    @BeforeClass
    public void registerProduct(){
        user = new User("Yan", "yan@gmail.com", "1234", "true");
        notAdminUser = new User("Eliane", "eliane@gmail.com", "1234", "false");
        userNotAuthenticated = new User("Ana", "ana@gmail.com", "1234", "true");

        product = new Product("XBOX ONE", "6000", "1TB storage", "800");
        product2 = new Product("Playstation 5", "8000", "1 TB storage", "40");
        fixedProduct = new Product("iPhone 11", "5800", "8gb ram, 128gb storage", "600");

        registerUserRequest(SPEC, user);
        registerUserRequest(SPEC, notAdminUser);
        registerUserRequest(SPEC, userNotAuthenticated);
        authenticateUserRequest(SPEC, user);
        authenticateUserRequest(SPEC, notAdminUser);
        authenticateUserRequest(SPEC, userNotAuthenticated);
        postProductRequest(SPEC, user, fixedProduct);
    }

    @AfterClass
    public void deleteProduct(){
        deleteProductRequest(SPEC, user, product);
    }

    @Test
    public void shouldReturnSuccessfulMessageAndStatus201() {
        postProductRequest(SPEC, user, product).
            then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_SUCCESS));
    }

    @Test
    public void shouldReturnFailMessageAndStatus400() {
        postProductRequest(SPEC, user, fixedProduct)
            .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_FAIL));
    }

    @Test
    public void shouldReturnFailMessageAndStatus403() {
        postProductRequest(SPEC, notAdminUser, product2)
            .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_UNAUTHORIZED));
    }

    @Test
    public void shouldReturnFailMessageAndStatus401() {
        userNotAuthenticated.setUserAuthToken("testing invalid token");
        postProductRequest(SPEC, userNotAuthenticated, product2)
            .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo(Constants.INVALID_TOKEN));
    }
}
