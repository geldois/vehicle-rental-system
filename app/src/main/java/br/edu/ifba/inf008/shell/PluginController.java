package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginController implements IPluginController
{
    @Override
    public boolean init() {
        try {
            File currentDir = new File("./plugins");

            // Tenta carregar plugins dinamicamente
            FilenameFilter jarFilter = (dir, name) -> name.toLowerCase().endsWith(".jar");
            String[] plugins = currentDir.list(jarFilter);

            // Fallback: nenhum plugin encontrado
            if (plugins == null || plugins.length == 0) {
                System.out.println("Nenhum plugin encontrado via loader. Carregando plugin padrão.");

                Class<?> clazz = Class.forName("br.edu.ifba.inf008.plugins.Economy");
                IPlugin economy = (IPlugin) clazz.getDeclaredConstructor().newInstance();
                economy.init();

                return true;
            }

            // Loader dinâmico (mantido, mas não crítico agora)
            URL[] jars = new URL[plugins.length];
            for (int i = 0; i < plugins.length; i++) {
                jars[i] = new File("./plugins/" + plugins[i]).toURI().toURL();
            }

            URLClassLoader ulc = new URLClassLoader(jars, App.class.getClassLoader());
            for (String plugin : plugins) {
                String pluginName = plugin.split("\\.")[0];
                IPlugin instance = (IPlugin) Class
                        .forName("br.edu.ifba.inf008.plugins." + pluginName, true, ulc)
                        .getDeclaredConstructor()
                        .newInstance();

                instance.init();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
