package requests;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Product;
import models.User;

import static io.restassured.RestAssured.given;

public class ProductEndpoint extends RequestBase {

    public static Response postProductRequest(RequestSpecification spec, User user, Product product) {

        Response productRequest =
                given()
                    .spec(spec)
                    .header("Content-Type","application/json")
                    .header("Authorization", user.authToken)
                .and()
                    .body(product.getProductInfo())
                .when()
                    .post("produtos");

        product.setProductId(getValueFromResponse(productRequest, "_id"));

        return productRequest;
    }
    public static  Response deleteProductRequest(RequestSpecification spec, User user, Product product) {
       Response productRequest =
            given()
                .spec(spec)
                .header("Content-Type","application/json")
                .header("Authorization", user.authToken)
            .and()
                .body(product.getProductInfo())
            .when()
                .delete("produtos/" + product.getIdProduct());

            return productRequest;
    }
}
