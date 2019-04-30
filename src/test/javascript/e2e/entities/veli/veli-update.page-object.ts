import { element, by, ElementFinder } from 'protractor';

export default class VeliUpdatePage {
  pageTitle: ElementFinder = element(by.id('ogrenciTakipSistemiApp.veli.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  adInput: ElementFinder = element(by.css('input#veli-ad'));
  soyadInput: ElementFinder = element(by.css('input#veli-soyad'));
  adresInput: ElementFinder = element(by.css('input#veli-adres'));
  meslegiInput: ElementFinder = element(by.css('input#veli-meslegi'));
  emailInput: ElementFinder = element(by.css('input#veli-email'));
  telefonCepInput: ElementFinder = element(by.css('input#veli-telefonCep'));
  telefonSabitInput: ElementFinder = element(by.css('input#veli-telefonSabit'));

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

  async setAdresInput(adres) {
    await this.adresInput.sendKeys(adres);
  }

  async getAdresInput() {
    return this.adresInput.getAttribute('value');
  }

  async setMeslegiInput(meslegi) {
    await this.meslegiInput.sendKeys(meslegi);
  }

  async getMeslegiInput() {
    return this.meslegiInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return this.emailInput.getAttribute('value');
  }

  async setTelefonCepInput(telefonCep) {
    await this.telefonCepInput.sendKeys(telefonCep);
  }

  async getTelefonCepInput() {
    return this.telefonCepInput.getAttribute('value');
  }

  async setTelefonSabitInput(telefonSabit) {
    await this.telefonSabitInput.sendKeys(telefonSabit);
  }

  async getTelefonSabitInput() {
    return this.telefonSabitInput.getAttribute('value');
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
