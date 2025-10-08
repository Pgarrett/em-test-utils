package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContributorKey {

    @JsonProperty("id")
    private Optional<Long> id;

    @JsonProperty("organizationId")
    private Optional<Long> organizationId;

    @JsonProperty("snapshotDate")
    private Optional<String> snapshotDate;

    public Optional<Long> getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<Long> getOrganizationid() {
        return organizationId;
    }
    
    public void setOrganizationid(Long organizationId) {
        this.organizationId = Optional.ofNullable(organizationId);
    }

    public Optional<String> getSnapshotdate() {
        return snapshotDate;
    }
    
    public void setSnapshotdate(String snapshotDate) {
        this.snapshotDate = Optional.ofNullable(snapshotDate);
    }

}
