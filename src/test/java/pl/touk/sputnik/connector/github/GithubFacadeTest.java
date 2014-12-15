package pl.touk.sputnik.connector.github;

import com.beust.jcommander.internal.Maps;
import com.jcabi.github.*;
import com.jcabi.github.Git;
import com.jcabi.http.wire.RetryWire;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.junit.Test;

import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
        /*for (Commit commit : pull.commits()) {
            RepoCommit.Smart repoCommit = new RepoCommit.Smart(repo.commits().get(commit.sha()));
            String baseSha = repoCommit.json().getJsonArray("parents").getJsonObject(0).getString("sha");
            String diff = repo.commits().diff(baseSha, commit.sha());
            diff.toCharArray();
        }*/


        try {
            pull.comments().post("Wrong indentation",
                    "460c8ee5124f5b3cea5d175c20222c7e0a6b4e79",
                    "src/main/java/pl/touk/sputnik/connector/gerrit/GerritFacadeBuilder.java", 1);
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

    private void fetchShaForCommits() throws Exception {
        File gitWorkDir = new File("/home/marcin/projects/sputnik");
        org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(gitWorkDir);

        String oldHash = "e26a7d333428bd3f7017afbff0208027fdd6ddf4";

        ObjectId headId = git.getRepository().resolve("HEAD^{tree}");
        ObjectId oldId = git.getRepository().resolve(oldHash + "^{tree}");

        ObjectReader reader = git.getRepository().newObjectReader();

        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(reader, oldId);
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, headId);

        List<DiffEntry> diffs= git.diff()
                .setNewTree(newTreeIter)
                .setOldTree(oldTreeIter)
                .call();
    }


}
