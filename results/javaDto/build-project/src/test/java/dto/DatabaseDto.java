package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatabaseDto {

    @JsonProperty("contributors")
    private Optional<List<Contributor>> contributors;

    @JsonProperty("projects")
    private Optional<List<Project>> projects;

    @JsonProperty("statistics")
    private Optional<List<Statistics>> statistics;

    public Optional<List<Contributor>> getContributors() {
        return contributors;
    }
    
    public void setContributors(List<Contributor> contributors) {
        this.contributors = Optional.ofNullable(contributors);
    }

    public Optional<List<Project>> getProjects() {
        return projects;
    }
    
    public void setProjects(List<Project> projects) {
        this.projects = Optional.ofNullable(projects);
    }

    public Optional<List<Statistics>> getStatistics() {
        return statistics;
    }
    
    public void setStatistics(List<Statistics> statistics) {
        this.statistics = Optional.ofNullable(statistics);
    }

}
