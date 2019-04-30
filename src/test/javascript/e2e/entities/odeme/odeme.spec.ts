/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import OdemeComponentsPage from './odeme.page-object';
import { OdemeDeleteDialog } from './odeme.page-object';
import OdemeUpdatePage from './odeme-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Odeme e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let odemeUpdatePage: OdemeUpdatePage;
  let odemeComponentsPage: OdemeComponentsPage;
  let odemeDeleteDialog: OdemeDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Odemes', async () => {
    await navBarPage.getEntityPage('odeme');
    odemeComponentsPage = new OdemeComponentsPage();
    expect(await odemeComponentsPage.getTitle().getText()).to.match(/Odemes/);
  });

  it('should load create Odeme page', async () => {
    await odemeComponentsPage.clickOnCreateButton();
    odemeUpdatePage = new OdemeUpdatePage();
    expect(await odemeUpdatePage.getPageTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.odeme.home.createOrEditLabel/);
  });

  it('should create and save Odemes', async () => {
    const nbButtonsBeforeCreate = await odemeComponentsPage.countDeleteButtons();

    await odemeUpdatePage.setTarihInput('01-01-2001');
    expect(await odemeUpdatePage.getTarihInput()).to.eq('2001-01-01');
    await odemeUpdatePage.setOdemeAdiInput('odemeAdi');
    expect(await odemeUpdatePage.getOdemeAdiInput()).to.match(/odemeAdi/);
    await odemeUpdatePage.setOdemeDetayiInput('odemeDetayi');
    expect(await odemeUpdatePage.getOdemeDetayiInput()).to.match(/odemeDetayi/);
    await odemeUpdatePage.setOdemeInput('5');
    expect(await odemeUpdatePage.getOdemeInput()).to.eq('5');
    await odemeUpdatePage.musteriSelectLastOption();
    await waitUntilDisplayed(odemeUpdatePage.getSaveButton());
    await odemeUpdatePage.save();
    await waitUntilHidden(odemeUpdatePage.getSaveButton());
    expect(await odemeUpdatePage.getSaveButton().isPresent()).to.be.false;

    await odemeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await odemeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Odeme', async () => {
    await odemeComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await odemeComponentsPage.countDeleteButtons();
    await odemeComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    odemeDeleteDialog = new OdemeDeleteDialog();
    expect(await odemeDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.odeme.delete.question/);
    await odemeDeleteDialog.clickOnConfirmButton();

    await odemeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await odemeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
