import { NgModule } from '@angular/core';

import { NgxMaskModule } from 'ngx-mask';
import { ButtonModule } from 'primeng/button';
import { AlertErrorComponent } from './alert/alert-error.component';
import { AlertComponent } from './alert/alert.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FilterComponent } from './filter/filter.component';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { SharedLibsModule } from './shared-libs.module';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';

@NgModule({
  imports: [SharedLibsModule, ButtonModule, NgxMaskModule.forRoot()],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
    ButtonModule,
    NgxMaskModule,
  ],
})
export class SharedModule {}
