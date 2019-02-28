import { NgModule } from '@angular/core';

import { NuxeoPerfSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [NuxeoPerfSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [NuxeoPerfSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class NuxeoPerfSharedCommonModule {}
