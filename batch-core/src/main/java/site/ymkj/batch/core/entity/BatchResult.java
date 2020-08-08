package site.ymkj.batch.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BatchResult {
  /**
   * 批处理整体状态
   */
  private BatchStatusEnum batchStatus;
  /**
   * 各阶段详细状态
   */
  private List<StageResult> stageResults;
}
