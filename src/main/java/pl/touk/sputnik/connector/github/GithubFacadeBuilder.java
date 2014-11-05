package pl.touk.sputnik.connector.github;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;
import com.jcabi.http.wire.RetryWire;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.configuration.CliOption;
import pl.touk.sputnik.configuration.ConfigurationHolder;
import pl.touk.sputnik.configuration.GeneralOption;
import pl.touk.sputnik.connector.ConnectorDetails;
import pl.touk.sputnik.connector.http.HttpConnector;
import pl.touk.sputnik.connector.http.HttpHelper;
import pl.touk.sputnik.connector.stash.StashConnector;
import pl.touk.sputnik.connector.stash.StashFacade;
import pl.touk.sputnik.connector.stash.StashPatchset;

import static org.apache.commons.lang3.Validate.notBlank;

public class GithubFacadeBuilder {

    @NotNull
    public GithubFacade build() {

        GithubPatchset githubPatchset = buildGithubPatchset();

        String oAuthKey = ConfigurationHolder.instance().getProperty(GeneralOption.GITHUB_API_KEY);
        Github github = new RtGithub(
                new RtGithub(oAuthKey)
                        .entry()
                        .through(RetryWire.class)
        );

        Repo repo = github.repos().get(new Coordinates.Simple(githubPatchset.projectPath));
        return new GithubFacade(new GithubConnector(repo, githubPatchset));
    }

    @NotNull
    public GithubPatchset buildGithubPatchset() {
        String pullRequestId = ConfigurationHolder.instance().getProperty(CliOption.PULL_REQUEST_ID);
        String repositorySlug = ConfigurationHolder.instance().getProperty(GeneralOption.REPOSITORY_SLUG);
        String projectKey = ConfigurationHolder.instance().getProperty(GeneralOption.PROJECT_KEY);

        notBlank(pullRequestId, "You must provide non blank Github pull request id");
        notBlank(repositorySlug, "You must provide non blank Github repository slug");
        notBlank(projectKey, "You must provide non blank Github project key");

        return new GithubPatchset(pullRequestId, repositorySlug, projectKey);
    }
}
