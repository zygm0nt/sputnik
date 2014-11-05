package pl.touk.sputnik.connector.github;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Pull;
import com.jcabi.github.Repo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.connector.Connector;
import pl.touk.sputnik.connector.http.HttpConnector;
import pl.touk.sputnik.connector.stash.StashPatchset;

import javax.json.JsonObject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

@Slf4j
@AllArgsConstructor
public class GithubConnector implements Connector {

    private Repo repo;

    private GithubPatchset githubPatchset;

    @NotNull
    @Override
    public String listFiles() throws URISyntaxException, IOException {
        Pull pull = repo.pulls().get(githubPatchset.pullRequestId);

        StringBuilder sb = new StringBuilder();
        Iterator<JsonObject> iterator = pull.files().iterator();

        // TODO
    }

    @NotNull
    @Override
    public String sendReview(String reviewInputAsJson) throws URISyntaxException, IOException {
        return null;
    }
}
