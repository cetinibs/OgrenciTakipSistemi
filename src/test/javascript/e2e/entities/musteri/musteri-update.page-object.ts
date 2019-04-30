import { element, by, ElementFinder } from 'protractor';

export default class MusteriUpdatePage {
  pageTitle: ElementFinder = element(by.id('ogrenciTakipSistemiApp.musteri.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  adInput: ElementFinder = element(by.css('input#musteri-ad'));
  soyadInput: ElementFinder = element(by.css('input#musteri-soyad'));
  veliSelect: ElementFinder = element(by.css('select#musteri-veli'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setAdInput(ad) {
    await this.adInput.sendKeys(ad);
  }

  async getAdInput() {
    return this.adInput.getAttribute('value');
  }

  async setSoyadInput(soyad) {
    await this.soyadInput.sendKeys(soyad);
  }

  async getSoyadInput() {
    return this.soyadInput.getAttribute('value');
  }

  async veliSelectLastOption() {
    await this.veliSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async veliSelectOption(option) {
    await this.veliSelect.sendKeys(option);
  }

  getVeliSelect() {
    return this.veliSelect;
  }

  async getVeliSelectedOption() {
    return this.veliSelect.element(by.css('option:checked')).getText();
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
