package com.sxc.gateway.pipe;

import com.mermaid.framework.core.mvc.APIResponse;
import com.sxc.gateway.pipe.input.IPipeInput;

public interface IPipe<T> {

    APIResponse<T> doPipe(IPipeInput input);

}
