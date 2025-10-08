package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {

    @JsonProperty("commitsCount")
    private Optional<Integer> commitsCount;

    @JsonProperty("contributorsCount")
    private Optional<Integer> contributorsCount;

    @JsonProperty("description")
    private Optional<String> description;

    @JsonProperty("externalContributorsCount")
    private Optional<Integer> externalContributorsCount;

    @JsonProperty("forksCount")
    private Optional<Integer> forksCount;

    @JsonProperty("gitHubProjectId")
    private Optional<Long> gitHubProjectId;

    @JsonProperty("image")
    private Optional<String> image;

    @JsonProperty("languageList")
    private Optional<List<String>> languageList;

    @JsonProperty("lastPushed")
    private Optional<String> lastPushed;

    @JsonProperty("maintainers")
    private Optional<List<String>> maintainers;

    @JsonProperty("name")
    private Optional<String> name;

    @JsonProperty("organizationName")
    private Optional<String> organizationName;

    @JsonProperty("primaryLanguage")
    private Optional<String> primaryLanguage;

    @JsonProperty("score")
    private Optional<Integer> score;

    @JsonProperty("snapshotDate")
    private Optional<String> snapshotDate;

    @JsonProperty("starsCount")
    private Optional<Integer> starsCount;

    @JsonProperty("title")
    private Optional<String> title;

    @JsonProperty("url")
    private Optional<String> url;

    public Optional<Integer> getCommitscount() {
        return commitsCount;
    }
    
    public void setCommitscount(Integer commitsCount) {
        this.commitsCount = Optional.ofNullable(commitsCount);
    }

    public Optional<Integer> getContributorscount() {
        return contributorsCount;
    }
    
    public void setContributorscount(Integer contributorsCount) {
        this.contributorsCount = Optional.ofNullable(contributorsCount);
    }

    public Optional<String> getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = Optional.ofNullable(description);
    }

    public Optional<Integer> getExternalcontributorscount() {
        return externalContributorsCount;
    }
    
    public void setExternalcontributorscount(Integer externalContributorsCount) {
        this.externalContributorsCount = Optional.ofNullable(externalContributorsCount);
    }

    public Optional<Integer> getForkscount() {
        return forksCount;
    }
    
    public void setForkscount(Integer forksCount) {
        this.forksCount = Optional.ofNullable(forksCount);
    }

    public Optional<Long> getGithubprojectid() {
        return gitHubProjectId;
    }
    
    public void setGithubprojectid(Long gitHubProjectId) {
        this.gitHubProjectId = Optional.ofNullable(gitHubProjectId);
    }

    public Optional<String> getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = Optional.ofNullable(image);
    }

    public Optional<List<String>> getLanguagelist() {
        return languageList;
    }
    
    public void setLanguagelist(List<String> languageList) {
        this.languageList = Optional.ofNullable(languageList);
    }

    public Optional<String> getLastpushed() {
        return lastPushed;
    }
    
    public void setLastpushed(String lastPushed) {
        this.lastPushed = Optional.ofNullable(lastPushed);
    }

    public Optional<List<String>> getMaintainers() {
        return maintainers;
    }
    
    public void setMaintainers(List<String> maintainers) {
        this.maintainers = Optional.ofNullable(maintainers);
    }

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<String> getOrganizationname() {
        return organizationName;
    }
    
    public void setOrganizationname(String organizationName) {
        this.organizationName = Optional.ofNullable(organizationName);
    }

    public Optional<String> getPrimarylanguage() {
        return primaryLanguage;
    }
    
    public void setPrimarylanguage(String primaryLanguage) {
        this.primaryLanguage = Optional.ofNullable(primaryLanguage);
    }

    public Optional<Integer> getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = Optional.ofNullable(score);
    }

    public Optional<String> getSnapshotdate() {
        return snapshotDate;
    }
    
    public void setSnapshotdate(String snapshotDate) {
        this.snapshotDate = Optional.ofNullable(snapshotDate);
    }

    public Optional<Integer> getStarscount() {
        return starsCount;
    }
    
    public void setStarscount(Integer starsCount) {
        this.starsCount = Optional.ofNullable(starsCount);
    }

    public Optional<String> getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = Optional.ofNullable(title);
    }

    public Optional<String> getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = Optional.ofNullable(url);
    }

}
