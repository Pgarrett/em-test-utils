package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contributor {

    @JsonProperty("id")
    private Optional<Long> id;

    @JsonProperty("key")
    private Optional<ContributorKey> key;

    @JsonProperty("loginId")
    private Optional<String> loginId;

    @JsonProperty("name")
    private Optional<String> name;

    @JsonProperty("organizationId")
    private Optional<Long> organizationId;

    @JsonProperty("organizationName")
    private Optional<String> organizationName;

    @JsonProperty("organizationalCommitsCount")
    private Optional<Integer> organizationalCommitsCount;

    @JsonProperty("organizationalProjectsCount")
    private Optional<Integer> organizationalProjectsCount;

    @JsonProperty("personalCommitsCount")
    private Optional<Integer> personalCommitsCount;

    @JsonProperty("personalProjectsCount")
    private Optional<Integer> personalProjectsCount;

    @JsonProperty("snapshotDate")
    private Optional<String> snapshotDate;

    @JsonProperty("url")
    private Optional<String> url;

    public Optional<Long> getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<ContributorKey> getKey() {
        return key;
    }
    
    public void setKey(ContributorKey key) {
        this.key = Optional.ofNullable(key);
    }

    public Optional<String> getLoginid() {
        return loginId;
    }
    
    public void setLoginid(String loginId) {
        this.loginId = Optional.ofNullable(loginId);
    }

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<Long> getOrganizationid() {
        return organizationId;
    }
    
    public void setOrganizationid(Long organizationId) {
        this.organizationId = Optional.ofNullable(organizationId);
    }

    public Optional<String> getOrganizationname() {
        return organizationName;
    }
    
    public void setOrganizationname(String organizationName) {
        this.organizationName = Optional.ofNullable(organizationName);
    }

    public Optional<Integer> getOrganizationalcommitscount() {
        return organizationalCommitsCount;
    }
    
    public void setOrganizationalcommitscount(Integer organizationalCommitsCount) {
        this.organizationalCommitsCount = Optional.ofNullable(organizationalCommitsCount);
    }

    public Optional<Integer> getOrganizationalprojectscount() {
        return organizationalProjectsCount;
    }
    
    public void setOrganizationalprojectscount(Integer organizationalProjectsCount) {
        this.organizationalProjectsCount = Optional.ofNullable(organizationalProjectsCount);
    }

    public Optional<Integer> getPersonalcommitscount() {
        return personalCommitsCount;
    }
    
    public void setPersonalcommitscount(Integer personalCommitsCount) {
        this.personalCommitsCount = Optional.ofNullable(personalCommitsCount);
    }

    public Optional<Integer> getPersonalprojectscount() {
        return personalProjectsCount;
    }
    
    public void setPersonalprojectscount(Integer personalProjectsCount) {
        this.personalProjectsCount = Optional.ofNullable(personalProjectsCount);
    }

    public Optional<String> getSnapshotdate() {
        return snapshotDate;
    }
    
    public void setSnapshotdate(String snapshotDate) {
        this.snapshotDate = Optional.ofNullable(snapshotDate);
    }

    public Optional<String> getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = Optional.ofNullable(url);
    }

}
