package com.montrosesoftware.dbassist.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@Mojo(name = "testAll")
public class TestAllVersions extends AbstractMojo {

    final static Logger logger = Logger.getLogger(TestAllVersions.class.getName());

    @Parameter(property = "testAll.projectNames")
    private List<String> projectNames;

    @Parameter(property = "testAll.directory")
    private String directory;

    public void execute() throws MojoExecutionException {
        if (projectNames == null || projectNames.isEmpty()) {
            return;
        }

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(directory + "/pom.xml"));
        request.setGoals(Arrays.asList("clean", "test"));

        for (String name : projectNames) {
            getLog().info("---------------- Testing for " + name + " ----------------");

            Properties properties = new Properties();
            properties.setProperty("montrosesoftware.version", name);
            request.setProperties(properties);

            Invoker invoker = new DefaultInvoker();
            try {
                InvocationResult result = invoker.execute(request);
                if(result.getExitCode() != 0){
                    throw new RuntimeException("Tests failed for " + name);
                }
            } catch (MavenInvocationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
