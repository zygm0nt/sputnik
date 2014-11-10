package pl.touk.sputnik.connector.github;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.jcabi.github.Pull;
import com.jcabi.github.PullComment;
import com.jcabi.github.PullComments;
import com.jcabi.github.Repo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.connector.ConnectorFacade;
import pl.touk.sputnik.connector.Connectors;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.ReviewFile;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class GithubFacade implements ConnectorFacade {

    private Repo repo;

    private GithubPatchset githubPatchset;

    @Override
    public Connectors name() {
        return Connectors.GITHUB;
    }

    @NotNull
    @Override
    public List<ReviewFile> listFiles() {
        Pull pull = getPull();

        List<ReviewFile> files = Lists.newArrayList();
        try {
            for (JsonObject o : pull.files()) {
                files.add(new ReviewFile(o.getString("filename")));
            }
        } catch (IOException ex) {
           log.error("Error fetching files for pull request", ex);
        }
        return files;
    }

    @Override
    public void setReview(@NotNull Review review) {
        try {
            PullComments comments = getPull().comments();
            for (PullComment c : comments.iterate(Maps.<String, String>newHashMap())) {
                c.number();
            }
        } catch (IOException e) {
            log.error("Error!!", e);
        }

        try {
            getPull().comments().post("This review generated " + review.getMessages().size() + " comments",
                    getPull().commits().iterator().next().sha(),
                    "src/main/java/pl/touk/sputnik/review/Engine.java", 27);
        } catch (IOException e) {
            log.error("Error adding comment", e);
        }
    }

    private Pull getPull() {
        return repo.pulls().get(githubPatchset.pullRequestId);
    }
}
