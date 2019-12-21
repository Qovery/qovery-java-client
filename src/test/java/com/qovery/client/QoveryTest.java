package com.qovery.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by evoxmusic on 19/12/2019.
 */
public class QoveryTest {

    private File LOCAL_CONFIGURATION_FILE;

    private File getFile(String fileName) {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }

    private String getFileContent(String fileName) {
        try (Stream<String> lines = Files.lines(getFile(fileName).toPath())) {
            return lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("QOVERY_JSON_B64", getFileContent("b64.txt"));
        System.setProperty("QOVERY_BRANCH_NAME", "master");
        System.setProperty("QOVERY_IS_PRODUCTION", "true");
        LOCAL_CONFIGURATION_FILE = getFile("local_configuration.json");
    }

    @Test
    public void loadConfigurationFromBadFile() {
        Qovery qovery = new Qovery(new File(""), false);
        Assert.assertNull(qovery.getConfiguration());
        Assert.assertNull(qovery.getDatabaseConfiguration("toto"));
    }

    @Test
    public void loadConfigurationFromFile() {
        Qovery qovery = new Qovery(LOCAL_CONFIGURATION_FILE, false);
        Assert.assertNotNull(qovery.getConfiguration());
    }

    @Test
    public void loadConfigurationFromEnvironmentVariableInParameters() {
        Qovery qovery = new Qovery(LOCAL_CONFIGURATION_FILE, false);
        Assert.assertNotNull(qovery.getConfiguration());
    }

    @Test
    public void isProductionTrue() {
        Qovery qovery = new Qovery(LOCAL_CONFIGURATION_FILE, false);
        Assert.assertTrue(qovery.isProduction());
    }

    @Test
    public void isBranchNameMaster() {
        Qovery qovery = new Qovery(LOCAL_CONFIGURATION_FILE, false);
        Assert.assertEquals(qovery.getBranchName(), "master");
    }

    @Test
    public void getDatabaseConfigurationByName() {
        Qovery qovery = new Qovery(LOCAL_CONFIGURATION_FILE, false);
        Assert.assertNotNull(qovery.getDatabaseConfiguration("my-pql"));
    }

    @Test
    public void databaseConfigurationEqualsNull() {
        Qovery qovery = new Qovery(LOCAL_CONFIGURATION_FILE, false);
        Assert.assertNull(qovery.getDatabaseConfiguration("not-existing"));
    }

    @Test
    public void listDatabaseConfiguration() {
        Qovery qovery = new Qovery(LOCAL_CONFIGURATION_FILE, false);
        Assert.assertEquals(qovery.listDatabaseConfiguration().count(), 1);
    }

}
