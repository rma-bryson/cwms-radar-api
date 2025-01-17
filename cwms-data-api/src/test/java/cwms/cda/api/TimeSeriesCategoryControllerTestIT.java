/*
 * MIT License
 *
 * Copyright (c) 2023 Hydrologic Engineering Center
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cwms.cda.api;

import fixtures.TestAccounts;
import io.restassured.filter.log.LogDetail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import cwms.cda.data.dto.TimeSeriesCategory;
import cwms.cda.formatters.ContentType;
import cwms.cda.formatters.Formats;

import javax.servlet.http.HttpServletResponse;

import static cwms.cda.api.Controllers.CASCADE_DELETE;
import static cwms.cda.api.Controllers.OFFICE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Tag("integration")
@Disabled // issue with groups and categories not affecting current changes with office
class TimeSeriesCategoryControllerTestIT extends DataApiTestIT
{

    @Test
    void test_create_read_delete() throws Exception {
        String officeId = "SPK";
        TestAccounts.KeyUser user = TestAccounts.KeyUser.SPK_NORMAL;
        TimeSeriesCategory cat = new TimeSeriesCategory(officeId, "test_create_read_delete", "IntegrationTesting");
        ContentType contentType = Formats.parseHeader(Formats.JSON, TimeSeriesCategory.class);
        String xml = Formats.format(contentType, cat);
        //Create Category
        given()
            .log().ifValidationFails(LogDetail.ALL,true)
            .accept(Formats.JSON)
            .contentType(Formats.JSON)
            .body(xml)
            .header("Authorization", user.toHeaderValue())
        .when()
            .redirects().follow(true)
            .redirects().max(3)
            .post("/timeseries/category")
        .then()
            .assertThat()
            .log().ifValidationFails(LogDetail.ALL,true)
            .statusCode(is(HttpServletResponse.SC_CREATED));
        //Read
        given()
            .log().ifValidationFails(LogDetail.ALL,true)
            .accept(Formats.JSON)
            .contentType(Formats.JSON)
            .queryParam(OFFICE, officeId)
        .when()
            .redirects().follow(true)
            .redirects().max(3)
            .get("/timeseries/category/" + cat.getId())
        .then()
            .assertThat()
            .log().ifValidationFails(LogDetail.ALL,true)
            .statusCode(is(HttpServletResponse.SC_OK))
            .body("office-id", equalTo(cat.getOfficeId()))
            .body("id", equalTo(cat.getId()))
            .body("description", equalTo(cat.getDescription()));
        //Delete
        given()
            .log().ifValidationFails(LogDetail.ALL,true)
            .accept(Formats.JSON)
            .contentType(Formats.JSON)
            .header("Authorization", user.toHeaderValue())
            .queryParam(OFFICE, officeId)
            .queryParam(CASCADE_DELETE, "true")
        .when()
            .redirects().follow(true)
            .redirects().max(3)
            .delete("/timeseries/category/" + cat.getId())
        .then()
            .assertThat()
            .log().ifValidationFails(LogDetail.ALL,true)
            .statusCode(is(HttpServletResponse.SC_NO_CONTENT));

        //Read Empty
        given()
            .log().ifValidationFails(LogDetail.ALL,true)
            .accept(Formats.JSON)
            .contentType(Formats.JSON)
            .queryParam("office", officeId)
        .when()
            .redirects().follow(true)
            .redirects().max(3)
            .get("/timeseries/category/" + cat.getId())
        .then()
            .assertThat()
            .log().ifValidationFails(LogDetail.ALL,true)
            .statusCode(is(HttpServletResponse.SC_NOT_FOUND));
    }
}
