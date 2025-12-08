package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaintNestedDto {


    @JsonProperty("s0")
    private Optional<String> s0;

    @JsonProperty("s1")
    private Optional<String> s1;

    @JsonProperty("s2")
    private Optional<String> s2;

    @JsonProperty("s3")
    private Optional<String> s3;

    @JsonProperty("s4")
    private Optional<String> s4;

    @JsonProperty("s5")
    private Optional<String> s5;

    @JsonProperty("s6")
    private Optional<String> s6;

    @JsonProperty("s7")
    private Optional<String> s7;

    @JsonProperty("s8")
    private Optional<String> s8;

    @JsonProperty("s9")
    private Optional<String> s9;


    public Optional<String> getS0() {
        return s0;
    }

    public void setS0(String s0) {
        this.s0 = Optional.ofNullable(s0);
    }


    public Optional<String> getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = Optional.ofNullable(s1);
    }


    public Optional<String> getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = Optional.ofNullable(s2);
    }


    public Optional<String> getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = Optional.ofNullable(s3);
    }


    public Optional<String> getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = Optional.ofNullable(s4);
    }


    public Optional<String> getS5() {
        return s5;
    }

    public void setS5(String s5) {
        this.s5 = Optional.ofNullable(s5);
    }


    public Optional<String> getS6() {
        return s6;
    }

    public void setS6(String s6) {
        this.s6 = Optional.ofNullable(s6);
    }


    public Optional<String> getS7() {
        return s7;
    }

    public void setS7(String s7) {
        this.s7 = Optional.ofNullable(s7);
    }


    public Optional<String> getS8() {
        return s8;
    }

    public void setS8(String s8) {
        this.s8 = Optional.ofNullable(s8);
    }


    public Optional<String> getS9() {
        return s9;
    }

    public void setS9(String s9) {
        this.s9 = Optional.ofNullable(s9);
    }

}
