package cwms.cda.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import cwms.cda.api.errors.FieldException;
import cwms.cda.formatters.Formats;
import cwms.cda.formatters.annotations.FormattableWith;
import cwms.cda.formatters.json.JsonV1;
import cwms.cda.formatters.json.JsonV2;
import cwms.cda.formatters.xml.XMLv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "clob")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
@FormattableWith(contentType = Formats.JSON, formatter = JsonV1.class)
@FormattableWith(contentType = Formats.JSONV2, formatter = JsonV2.class)
@FormattableWith(contentType = Formats.XMLV2, formatter = XMLv2.class)
public class Clob extends CwmsDTO {
    @JsonProperty(required = true)
    private String id;
    private String description;
    private String value;

    @SuppressWarnings("unused")
    private Clob() {
        super(null);
    }

    public Clob(String office, String id, String description, String value) {
        super(office);
        this.id = id;
        this.description = description;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getOfficeId()).append("/").append(id).append(";description=").append(description);
        return builder.toString();
    }

    @Override
    public void validate() throws FieldException {
    }
}
