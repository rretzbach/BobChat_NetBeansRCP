package de.rretzbach.bobchat.irc;

import de.rretzbach.bobchat.core.Config;
import de.rretzbach.bobchat.core.NetworkListener;
import de.rretzbach.bobchat.core.NetworkTreeNodes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author rretzbach
 */
public class Router {
    private static Router instance;

    protected List<Network> networks = new ArrayList<Network>();
    
    public static Router get() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }
    private List<NetworkListener> listener = new ArrayList<NetworkListener>();
    
    public Network getNetwork(String hostname) {
        Network found = findNetwork(hostname);
        if (found == null) {
            Identity identity = Config.get().getIdentity(hostname);
            found = new Network(identity, hostname);
            networks.add(found);
            notifyListenersAdd(found);
        }

        return found;
    }

    private Network findNetwork(String hostname) {
        for (Network n : networks) {
            if (n.getHostname() != null && n.getHostname().equals(hostname)) {
                return n;
            }
        }
        return null;
    }

    public List<Network> getNetworks() {
        return Collections.unmodifiableList(networks);
    }

    public void addNetworkListener(NetworkListener listener) {
        this.listener.add(listener);
    }

    private void notifyListenersAdd(Network network) {
        for (NetworkListener networkListener : listener) {
            networkListener.addedNetwork(network);
        }
    }
}
