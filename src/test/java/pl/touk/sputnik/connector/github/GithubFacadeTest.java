package pl.touk.sputnik.connector.github;

import com.beust.jcommander.internal.Maps;
import com.jcabi.github.*;
import com.jcabi.http.wire.RetryWire;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class GithubFacadeTest {

    @Test
    public void shouldAddCommentToGithub() throws Exception {

        String oAuthKey = "2ef119ad4ccb1fc9e49571c4c9a8f72e62fcca72";
        Github github = new RtGithub(
                new RtGithub(oAuthKey)
                        .entry()
                        .through(RetryWire.class)
        );

        Repo repo = github.repos().get(new Coordinates.Simple("zygm0nt/sputnik"));
        Pull pull = repo.pulls().get(2);
        try {
            PullComments comments = pull.comments();
            for (PullComment c : comments.iterate(Maps.<String, String>newHashMap())) {
                c.number();
            }
        } catch (IOException e) {
            log.error("Error!!", e);
        }

        try {
            pull.comments().post("Wrong indentation",
                    "460c8ee5124f5b3cea5d175c20222c7e0a6b4e79",
                    "src/main/java/pl/touk/sputnik/connector/gerrit/GerritFacadeBuilder.java", 5);
        } catch (IOException e) {
            log.error("Error adding comment", e);
        }
    }

    /*
    fetch commits for a pull request
    fetch each commits change files
    check which file has most recent change for line X (iterate for all lines)
    create requests to github with correct commit_id, position, file


    https://api.github.com/repos/zygm0nt/sputnik/commits/410266112b6d734a76387a5751058d2f8bdd5511
     */

}
