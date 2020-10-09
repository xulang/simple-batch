package site.ymkj.batch.core.test;

import org.junit.Test;
import site.ymkj.batch.core.DefaultBatchManager;
import site.ymkj.batch.core.IBatchManager;
import site.ymkj.batch.core.IProcessor;
import site.ymkj.batch.core.entity.StageResult;
import site.ymkj.batch.core.store.MemoryStore;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BatchTest {

	@Test
	public void test() throws ExecutionException, InterruptedException {
		IBatchManager batchManage = new DefaultBatchManager(new MemoryStore());
		IProcessor<TestArgs> processor = new TestProcessor(new TestArgs("test"));
		String batchId = batchManage.submitBatch(processor);
		// 同步等待批处理结果
		List<StageResult> stageResults = processor.getFuture().get();
		assertTrue(stageResults != null);
		assertTrue(stageResults.size() == 2);
		assertTrue(stageResults.get(0).getStageReturn().equals("stage1test"));
		assertTrue(stageResults.get(1).getStageReturn().equals("stage2test"));
	}
}
