package site.ymkj.batch.core.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.ymkj.batch.core.entity.ProcessArgs;

@AllArgsConstructor
@Data
public class TestArgs extends ProcessArgs {
  private String arg1;
}
