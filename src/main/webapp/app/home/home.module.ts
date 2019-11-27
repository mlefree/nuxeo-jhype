import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NuxeoPerfSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';


import '@nuxeo/nuxeo-elements/core/nuxeo-elements.js';
import '@nuxeo/nuxeo-elements/ui/nuxeo-ui-elements.js';

@NgModule({
    imports: [NuxeoPerfSharedModule, RouterModule.forChild([HOME_ROUTE])],
    declarations: [HomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NuxeoPerfHomeModule {}
