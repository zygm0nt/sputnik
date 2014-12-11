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
        for (Commit commit : pull.commits()) {
        }
        try {
            PullComments comments = pull.comments();
            for (PullComment c : comments.iterate(Maps.<String, String>newHashMap())) {
                c.number();
            }
        } catch (IOException e) {
            log.error("Error!!", e);
        }

        try {
            pull.comments().post("This review generated 2 comments",
                    "faa1c3e",
                    "src/main/java/pl/touk/sputnik/review/Engine.java", 30);
        } catch (IOException e) {
            log.error("Error adding comment", e);
        }
    }
/*
    @Test
    public void shouldAddCommentDifferentLib() throws Exception {
        GitHub github = GitHub.connect("zygm0nt", "2ef119ad4ccb1fc9e49571c4c9a8f72e62fcca72");
        GHRepository repo = github.getRepository("zygm0nt/sputnik");
        GHPullRequest pullRequest = repo.getPullRequest(2);
        PagedIterable<GHPullRequestCommitDetail> iter = pullRequest.listCommits();

        for (GHPullRequestCommitDetail commitDetail : iter) {
            commitDetail.getCommit();
        }
    }*/

}
