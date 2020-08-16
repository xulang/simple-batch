package site.ymkj.batch.core.test;

import site.ymkj.batch.core.AbstractProcessor;
import site.ymkj.batch.core.IStage;
import site.ymkj.batch.core.entity.BatchContext;
import site.ymkj.batch.core.entity.ProcessArgs;
import site.ymkj.batch.core.entity.StageResult;

import java.util.HashMap;
import java.util.Map;

public class TestProcessor extends AbstractProcessor<TestArgs> {
  public TestProcessor(TestArgs processArgs) {
    super(processArgs);
  }

  @Override
  protected Map<String, Object> initBatchContext() {
    return new HashMap<>();
  }

  @Override
  protected void initStage() {
    addStage(new IStage<TestArgs>() {
      @Override
      public String name() {
        return "stage1";
      }

      @Override
      public Object doStage(TestArgs args, StageResult lastStageResult, BatchContext batchContext) {
        return name() + args.getArg1();
      }

    });

    addStage(new IStage<TestArgs>() {

      @Override
      public String name() {
        return "stage2";
      }

      @Override
      public Object doStage(TestArgs args, StageResult lastStageResult, BatchContext batchContext) {
        return name() + args.getArg1();
      }
    });
  }

  @Override
  public String type() {
    return "test";
  }
}
