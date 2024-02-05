package org.example.util;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.walk;
import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

public class BenchMarkExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final String START_TIME = "start_time";

  public static final String RESOURCES_SNAPSHOTS = "src/test/resources/snapshots";

  private static final Namespace NAMESPACE = Namespace.create(BenchMarkExtension.class);

  private static final Map<String, Snapshot> snapshotMap;

  static {
   snapshotMap = loadStoredSnapshots();
  }

  public BenchMarkExtension() throws IOException {
    this.createSnapshotDirectoryIfNecessary();
  }

  @Override
  public void beforeTestExecution(final ExtensionContext context) throws Exception {

    final Method testMethod = context.getRequiredTestMethod();
    final Store  store      = context.getStore(NAMESPACE);

    if (!shouldBeBenchmarked(context)) {
      return;
    }

    this.createSnapshotTestFileIfNecessary(testMethod.getName());

    final Instant startTime = Instant.now();

    store.put(START_TIME, startTime);
  }

  private void createSnapshotDirectoryIfNecessary() throws IOException {

    final Path path = Paths.get(RESOURCES_SNAPSHOTS);

    if (!exists(path)) {
      createDirectories(path);
    }
  }

  private void createSnapshotTestFileIfNecessary(final String testName) throws IOException {
    final String snapshotPath = String.format("src/test/resources/snapshots/%s.snapshot", testName);
    final Path   path         = Paths.get(snapshotPath);
    if (!exists(path)) {
      createFile(path);
    }
  }

  @Override
  public void afterTestExecution(final ExtensionContext context) throws Exception {

    final Method testMethod = context.getRequiredTestMethod();

    if (!shouldBeBenchmarked(context)) {
      return;
    }

    final Store store = context.getStore(NAMESPACE);

    final Instant startTime = store.remove(START_TIME, Instant.class);

    final Duration elapsedTime = Duration.between(startTime, Instant.now());

    final Snapshot snapshot = snapshotMap.get(testMethod.getName());

    if (this.shouldGenerateSnapshot(snapshot)) {
      this.generateSnapshot(testMethod.getName(), elapsedTime.toMillis());
      return;
    }

    final long elapsedTimeMillis        = elapsedTime.toMillis();
    final long lastRunElapsedTimeMillis = snapshot.lastRunElapsedTime().toMillis();

    if (elapsedTimeMillis > lastRunElapsedTimeMillis) {

      final double increasedPercentage =
          this.getPercentageIncrease(lastRunElapsedTimeMillis, elapsedTimeMillis);

      if(this.isIncreasePercentageAllowed(increasedPercentage, testMethod)){
        return;
      }

      throw Failures.instance()
          .failure(String.format("The test method %s increased by %.2f%%.", testMethod.getName(), increasedPercentage));
    }
  }

  private boolean isIncreasePercentageAllowed(final double increasedPercentage, final Method testMethod) {
    final BenchMark benchMark = testMethod.getAnnotation(BenchMark.class);
    return increasedPercentage <= benchMark.percentageAllowed();
  }

  private boolean shouldGenerateSnapshot(final Snapshot snapshot) {
    return Objects.isNull(snapshot);
  }

  private void generateSnapshot(final String testName, final long elapsedTime) throws IOException {
    final String snapshotPath = String.format("src/test/resources/snapshots/%s.snapshot", testName);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(snapshotPath))) {
      final Snapshot snapshot = new Snapshot(testName, Instant.now(), Duration.ofMillis(elapsedTime));
      writer.write(snapshot.toString());
    }
  }

  private double getPercentageIncrease(final long initialDuration, final long actualDuration) {
    return ((actualDuration - initialDuration) / (double) initialDuration) * 100;
  }

  private static boolean shouldBeBenchmarked(ExtensionContext context) {
    return context.getElement()
        .map(value -> isAnnotated(value, BenchMark.class))
        .orElse(false);
  }

  private static Map<String, Snapshot> loadStoredSnapshots() {
    try {
      final Path pathToFile = Paths.get(RESOURCES_SNAPSHOTS);

      return getPaths(pathToFile).stream()
          .map(BenchMarkExtension::readAllLinesFromPath)
          .flatMap(List::stream)
          .map(BenchMarkExtension::toSnapshot)
          .collect(Collectors.toMap(Snapshot::name, Function.identity()));

    }catch (Exception e) {
      return Collections.emptyMap();
    }
  }

  private static List<Path> getPaths(final Path pathToFile) throws IOException {
    try (final var pathStream = walk(pathToFile).filter(Files::isRegularFile)) {
      return pathStream.toList();
    }
  }

  private static List<String> readAllLinesFromPath(final Path path) {
    try {
      return readAllLines(path, StandardCharsets.UTF_8);
    } catch (final Exception e) {
      return Collections.emptyList();
    }
  }

  private static Snapshot toSnapshot(final String line) {
    final String[] attributes = line.split(",");
    return createSnapshot(attributes);
  }

  private static Snapshot createSnapshot(final String[] attributes) {
    final String   name               = attributes[0];
    final Instant  lastExecutionDate  = Instant.parse(attributes[1]);
    final Duration lastRunElapsedTime = Duration.parse(attributes[2]);

    return new Snapshot(name, lastExecutionDate, lastRunElapsedTime);
  }

}
