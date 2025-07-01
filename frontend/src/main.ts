import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/components/app/app.component';
import { DefaultValueAccessor } from '@angular/forms';

// Apply the DefaultValueAccessor fix from the old AppModule
DefaultValueAccessor.prototype.registerOnChange = function (fn: (_: string | null) => void): void {
  this.onChange = (value: string | null) => {
      fn(value === '' ? null : value);
  };
};

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
