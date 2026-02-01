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
            File pluginsDir = new File("./plugins");

            if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
                System.out.println("Diretório ./plugins não encontrado.");
                return true;
            }

            File[] jars = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"));

            if (jars == null || jars.length == 0) {
                System.out.println("Nenhum plugin encontrado.");
                
                return true;
            }

            for (File jar : jars) {
                URL jarUrl = jar.toURI().toURL();
                URLClassLoader loader = new URLClassLoader(
                        new URL[]{jarUrl},
                        App.class.getClassLoader()
                );

                try (java.util.jar.JarFile jarFile = new java.util.jar.JarFile(jar)) {
                    jarFile.stream()
                            .filter(e -> e.getName().endsWith(".class"))
                            .forEach(entry -> {
                                try {
                                    String className = entry.getName()
                                            .replace("/", ".")
                                            .replace(".class", "");

                                    Class<?> _class = Class.forName(className, true, loader);

                                    if (IPlugin.class.isAssignableFrom(_class)) {
                                        IPlugin plugin = (IPlugin) _class
                                                .getDeclaredConstructor()
                                                .newInstance();

                                        plugin.init();
                                        System.out.println("Plugin carregado: " + _class.getName());
                                    }

                                } catch (Exception ignored) {
                                }
                            });
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            
            return false;
        }
    }
}
