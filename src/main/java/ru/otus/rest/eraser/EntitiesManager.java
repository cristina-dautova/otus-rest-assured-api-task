package ru.otus.rest.eraser;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.rest.exceptions.HandledFailure;

import java.util.*;

@NoArgsConstructor
public class EntitiesManager {

  private Logger logger = LogManager.getLogger(this.getClass());
  public final Set<EraseMethod> eraseMethodEntitiesCollection = new LinkedHashSet<>();
  private List<String> eraseErrorMessages = new ArrayList<>();

  public EntitiesManager(EntitiesManager entitiesManager) {
    this.eraseMethodEntitiesCollection.addAll(entitiesManager.eraseMethodEntitiesCollection);
    this.eraseErrorMessages = new ArrayList<>(entitiesManager.eraseErrorMessages);
  }

  public void addEraseMethodToMethodCollection(EraseMethod method) {
    eraseMethodEntitiesCollection.add(method);
  }

  public void eraseTestEntities() {
    eraseEntities(eraseMethodEntitiesCollection);
  }

  private void eraseEntities(Set<EraseMethod> methods) {
    Iterator<EraseMethod> itr = new LinkedList<>(methods).descendingIterator();
    while (itr.hasNext()) {
      EraseMethod method = itr.next();
      eraseEntityWithTry(method);
      methods.remove(method);
    }
  }

  private void eraseEntityWithTry(EraseMethod method) {

    try {
      method.erase();
    } catch (HandledFailure handledFailure) {
      logger.error("Entity was not deleted because: " + handledFailure);
      eraseErrorMessages.add(handledFailure.getErrorResponse().toString());
    }
  }

  public void validateEraseForErrors() {
    if (!eraseErrorMessages.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder("There are following errors during erasing entities:\n");
      eraseErrorMessages.forEach(item -> stringBuilder.append(String.format("%s%n", item)));
      eraseErrorMessages.clear();
      throw new IllegalStateException(stringBuilder.toString());
    }
  }
}
