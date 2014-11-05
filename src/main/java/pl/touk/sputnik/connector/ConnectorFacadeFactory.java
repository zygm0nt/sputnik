package pl.touk.sputnik.connector;

import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.connector.gerrit.GerritFacadeBuilder;
import pl.touk.sputnik.connector.github.GithubFacadeBuilder;
import pl.touk.sputnik.connector.stash.StashFacadeBuilder;

public class ConnectorFacadeFactory {
    public static final ConnectorFacadeFactory INSTANCE = new ConnectorFacadeFactory();

    private static final String GERRIT = "gerrit";
    private static final String STASH = "stash";
    private static final String GITHUB = "github";

    GerritFacadeBuilder gerritFacadeBuilder = new GerritFacadeBuilder();
    StashFacadeBuilder stashFacadeBuilder = new StashFacadeBuilder();
    GithubFacadeBuilder githubFacadeBuilder = new GithubFacadeBuilder();

    @NotNull
    public ConnectorFacade build(String key) {
        switch (key) {
            case STASH:
                return stashFacadeBuilder.build();
            case GERRIT:
                return gerritFacadeBuilder.build();
            case GITHUB:
                return githubFacadeBuilder.build();
            default:
                throw new IllegalArgumentException("Connector " + key + " is not supported");
        }
    }
}
