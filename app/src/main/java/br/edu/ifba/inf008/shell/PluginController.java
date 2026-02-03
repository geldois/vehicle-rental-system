package br.edu.ifba.inf008.shell;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;

public class PluginController implements IPluginController {
    
    @Override
    public boolean init() {
        try {
            java.util.ServiceLoader<IPlugin> loader =
                    java.util.ServiceLoader.load(IPlugin.class);

            boolean found = false;

            for (IPlugin plugin : loader) {
                found = true;
                plugin.init();
                System.out.println("Plugin carregado: " + plugin.getClass().getName());
            }

            if (!found) {
                System.out.println("Nenhum plugin encontrado.");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
