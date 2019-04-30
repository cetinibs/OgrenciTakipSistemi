import { element, by, ElementFinder } from 'protractor';

export default class OdemeUpdatePage {
  pageTitle: ElementFinder = element(by.id('ogrenciTakipSistemiApp.odeme.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  tarihInput: ElementFinder = element(by.css('input#odeme-tarih'));
  odemeAdiInput: ElementFinder = element(by.css('input#odeme-odemeAdi'));
  odemeDetayiInput: ElementFinder = element(by.css('input#odeme-odemeDetayi'));
  odemeInput: ElementFinder = element(by.css('input#odeme-odeme'));
  musteriSelect: ElementFinder = element(by.css('select#odeme-musteri'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setTarihInput(tarih) {
    await this.tarihInput.sendKeys(tarih);
  }

  async getTarihInput() {
    return this.tarihInput.getAttribute('value');
  }

  async setOdemeAdiInput(odemeAdi) {
    await this.odemeAdiInput.sendKeys(odemeAdi);
  }

  async getOdemeAdiInput() {
    return this.odemeAdiInput.getAttribute('value');
  }

  async setOdemeDetayiInput(odemeDetayi) {
    await this.odemeDetayiInput.sendKeys(odemeDetayi);
  }

  async getOdemeDetayiInput() {
    return this.odemeDetayiInput.getAttribute('value');
  }

  async setOdemeInput(odeme) {
    await this.odemeInput.sendKeys(odeme);
  }

  async getOdemeInput() {
    return this.odemeInput.getAttribute('value');
  }

  async musteriSelectLastOption() {
    await this.musteriSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async musteriSelectOption(option) {
    await this.musteriSelect.sendKeys(option);
  }

  getMusteriSelect() {
    return this.musteriSelect;
  }

  async getMusteriSelectedOption() {
    return this.musteriSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
