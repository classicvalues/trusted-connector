/*-
 * ========================LICENSE_START=================================
 * IDS Container Manager
 * %%
 * Copyright (C) 2017 Fraunhofer AISEC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package de.fhg.aisec.ids.rm;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.InterceptStrategy;

/**
 * 
 * @author Mathias Morbitzer (mathias.morbitzer@aisec.fraunhofer.de)
 */

public class CamelInterceptor implements InterceptStrategy {
	private RouteManagerService rm;

	public CamelInterceptor(RouteManagerService rm) {
		this.rm = rm;
	}
	
	@Override
    public Processor wrapProcessorInInterceptors(final CamelContext context, final ProcessorDefinition<?> definition,
                                                 final Processor target, final Processor nextTarget) throws Exception {
 
        return new PolicyEnforcementPoint(target, this.rm);
    }

}
