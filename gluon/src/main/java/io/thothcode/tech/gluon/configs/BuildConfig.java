package io.thothcode.tech.gluon.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource("classpath:git.properties")
public class BuildConfig {

    @Value("${git.build.time}")
    private String buildDate;

    @Value("${git.commit.id.abbrev}")
    private String commitID;

    @Value("${git.build.version}")
    private String version;

    @Value("${git.branch}")
    private String branch;


    public String getVersionString() {
        return String.format("Elvis - a small business management Server v%s, Build %s-%s, %s", getVersion(), getBuildDate(), getCommitID(), getBranch());
    }

}
