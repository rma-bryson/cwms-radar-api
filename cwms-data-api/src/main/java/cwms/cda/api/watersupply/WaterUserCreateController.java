/*
 *
 * MIT License
 *
 * Copyright (c) 2024 Hydrologic Engineering Center
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE
 * SOFTWARE.
 */

package cwms.cda.api.watersupply;

import static cwms.cda.api.Controllers.*;
import static cwms.cda.data.dao.JooqDao.getDslContext;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import cwms.cda.data.dao.watersupply.WaterContractDao;
import cwms.cda.data.dto.watersupply.WaterUser;
import cwms.cda.formatters.ContentType;
import cwms.cda.formatters.Formats;
import io.javalin.core.util.Header;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;


public final class WaterUserCreateController extends WaterSupplyControllerBase implements Handler {

    public WaterUserCreateController(MetricRegistry metrics) {
        waterMetrics(metrics);
    }

    @OpenApi(
        queryParams = {
            @OpenApiParam(name = FAIL_IF_EXISTS, description = "If true, the operation will fail if the water user "
                    + "already exists. Default: true", type = Boolean.class),
        },
        requestBody = @OpenApiRequestBody(
            content = {
                @OpenApiContent(from = WaterUser.class, type = Formats.JSONV1)
            },
            required = true),
        responses = {
            @OpenApiResponse(status = STATUS_204, description = "Water user successfully stored to CWMS."),
            @OpenApiResponse(status = STATUS_501, description = "Requested format is not implemented")
        },
        description = "Stores a water user to CWMS.",
        method = HttpMethod.POST,
        path = "/projects/{office}/{project-id}/water-user",
        tags = {TAG}
    )
    @Override
    public void handle(@NotNull Context ctx) {
        try (Timer.Context ignored = markAndTime(CREATE)) {
            DSLContext dsl = getDslContext(ctx);
            String formatHeader = ctx.req.getContentType();
            ContentType contentType = Formats.parseHeader(formatHeader, WaterUser.class);
            ctx.contentType(contentType.toString());
            WaterUser user = Formats.parseContent(contentType, ctx.body(), WaterUser.class);
            boolean failIfExists = Boolean.parseBoolean(ctx.queryParam(FAIL_IF_EXISTS));
            WaterContractDao contractDao = getContractDao(dsl);
            contractDao.storeWaterUser(user, failIfExists);
            ctx.status(HttpServletResponse.SC_CREATED).json(user.getEntityName() + " user created successfully.");
        }
    }
}