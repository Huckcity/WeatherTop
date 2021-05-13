import models.Member;

import play.jobs.*;
import play.test.*;

@OnApplicationStart
public class Bootstrap extends Job {
  public void doJob() {
    if (Member.count() == 0) {
      Fixtures.loadModels("data.yml");
    }
  }
}