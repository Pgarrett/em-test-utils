package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics {

    @JsonProperty("allContributorsCount")
    private Optional<Integer> allContributorsCount;

    @JsonProperty("allForksCount")
    private Optional<Integer> allForksCount;

    @JsonProperty("allSizeCount")
    private Optional<Integer> allSizeCount;

    @JsonProperty("allStarsCount")
    private Optional<Integer> allStarsCount;

    @JsonProperty("externalContributorsCount")
    private Optional<Integer> externalContributorsCount;

    @JsonProperty("id")
    private Optional<Long> id;

    @JsonProperty("key")
    private Optional<StatisticsKey> key;

    @JsonProperty("membersCount")
    private Optional<Integer> membersCount;

    @JsonProperty("organizationName")
    private Optional<String> organizationName;

    @JsonProperty("privateProjectCount")
    private Optional<Integer> privateProjectCount;

    @JsonProperty("programLanguagesCount")
    private Optional<Integer> programLanguagesCount;

    @JsonProperty("publicProjectCount")
    private Optional<Integer> publicProjectCount;

    @JsonProperty("snapshotDate")
    private Optional<String> snapshotDate;

    @JsonProperty("tagsCount")
    private Optional<Integer> tagsCount;

    @JsonProperty("teamsCount")
    private Optional<Integer> teamsCount;

    public Optional<Integer> getAllcontributorscount() {
        return allContributorsCount;
    }
    
    public void setAllcontributorscount(Integer allContributorsCount) {
        this.allContributorsCount = Optional.ofNullable(allContributorsCount);
    }

    public Optional<Integer> getAllforkscount() {
        return allForksCount;
    }
    
    public void setAllforkscount(Integer allForksCount) {
        this.allForksCount = Optional.ofNullable(allForksCount);
    }

    public Optional<Integer> getAllsizecount() {
        return allSizeCount;
    }
    
    public void setAllsizecount(Integer allSizeCount) {
        this.allSizeCount = Optional.ofNullable(allSizeCount);
    }

    public Optional<Integer> getAllstarscount() {
        return allStarsCount;
    }
    
    public void setAllstarscount(Integer allStarsCount) {
        this.allStarsCount = Optional.ofNullable(allStarsCount);
    }

    public Optional<Integer> getExternalcontributorscount() {
        return externalContributorsCount;
    }
    
    public void setExternalcontributorscount(Integer externalContributorsCount) {
        this.externalContributorsCount = Optional.ofNullable(externalContributorsCount);
    }

    public Optional<Long> getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<StatisticsKey> getKey() {
        return key;
    }
    
    public void setKey(StatisticsKey key) {
        this.key = Optional.ofNullable(key);
    }

    public Optional<Integer> getMemberscount() {
        return membersCount;
    }
    
    public void setMemberscount(Integer membersCount) {
        this.membersCount = Optional.ofNullable(membersCount);
    }

    public Optional<String> getOrganizationname() {
        return organizationName;
    }
    
    public void setOrganizationname(String organizationName) {
        this.organizationName = Optional.ofNullable(organizationName);
    }

    public Optional<Integer> getPrivateprojectcount() {
        return privateProjectCount;
    }
    
    public void setPrivateprojectcount(Integer privateProjectCount) {
        this.privateProjectCount = Optional.ofNullable(privateProjectCount);
    }

    public Optional<Integer> getProgramlanguagescount() {
        return programLanguagesCount;
    }
    
    public void setProgramlanguagescount(Integer programLanguagesCount) {
        this.programLanguagesCount = Optional.ofNullable(programLanguagesCount);
    }

    public Optional<Integer> getPublicprojectcount() {
        return publicProjectCount;
    }
    
    public void setPublicprojectcount(Integer publicProjectCount) {
        this.publicProjectCount = Optional.ofNullable(publicProjectCount);
    }

    public Optional<String> getSnapshotdate() {
        return snapshotDate;
    }
    
    public void setSnapshotdate(String snapshotDate) {
        this.snapshotDate = Optional.ofNullable(snapshotDate);
    }

    public Optional<Integer> getTagscount() {
        return tagsCount;
    }
    
    public void setTagscount(Integer tagsCount) {
        this.tagsCount = Optional.ofNullable(tagsCount);
    }

    public Optional<Integer> getTeamscount() {
        return teamsCount;
    }
    
    public void setTeamscount(Integer teamsCount) {
        this.teamsCount = Optional.ofNullable(teamsCount);
    }

}
