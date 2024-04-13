package cwms.cda.data.dao;


import cwms.cda.data.dto.forecast.ForecastInstance;
import cwms.cda.data.dto.forecast.ForecastSpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jooq.DSLContext;

public class MockForecastInstanceDao extends ForecastInstanceDao {

    public MockForecastInstanceDao(DSLContext dsl) {
        super(dsl);
    }

    public void create(ForecastInstance forecastInst) {

    }

    public List<ForecastInstance> getForecastInstances(String office, String name, String designator) {
        List<ForecastInstance> retval = new ArrayList<>();

        retval.add(buildInstance(office, "test_spec_1", designator, Instant.parse("2021-06-21T14:00:00Z"), Instant.parse("2022-05-22T12:00:00Z")));

        return retval;
    }

    public ForecastInstance getForecastInstance(String office, String name, String designator,
                                                String forecastDate, String issueDate) {

        return buildInstance(office, name, designator, Instant.parse("2021-06-21T14:00:00Z"), Instant.parse("2022-05-22T12:00:00Z"));
    }

    private static ForecastInstance buildInstance(String office, String name, String designator, Instant dateTime, Instant issueDate) {
//        Instant dateTime = Instant.parse("2021-06-21T14:00:10Z");
//        Instant issueDateTime = Instant.parse("2022-05-22T12:03:40Z");
        Instant firstDateTime = Instant.parse("2023-08-22T11:00:00Z");
        Instant lastDateTime = Instant.parse("2024-09-22T15:00:00Z");

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", "value2");
        metadata.put("key3", "value3");

        ForecastSpec spec = MockForecastSpecDao.buildForecastSpec(office, name, designator,
                Arrays.asList("Spec1TestLocationA.Flow.Inst.1Hour.0.raw","Spec1TestLocationB.Flow.Inst.1Hour.0.raw"),
                Arrays.asList("Spec1TestLocationA", "Spec1TestLocationB"));

        return new ForecastInstance.Builder()
                .withSpec(spec)
                .withDateTime(dateTime)
                .withIssueDateTime(issueDate)
                .withFirstDateTime(firstDateTime)
                .withLastDateTime(lastDateTime)
                .withMaxAge(5)
                .withTimeSeriesCount(2)
                .withNotes("test notes")
                .withMetadata(metadata)
                .withFilename("testFilename.txt")
                .withFileDescription("test file description")
                .withFileData("test file content".getBytes(StandardCharsets.UTF_8))
                .withFileDataUrl(null)
                .build();
    }

    public void update(ForecastInstance forecastInst) {
    }

    public void delete(String office, String name, String designator,
                       String forecastDate, String issueDate) {
    }

}
