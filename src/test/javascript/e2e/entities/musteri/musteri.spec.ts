/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import MusteriComponentsPage from './musteri.page-object';
import { MusteriDeleteDialog } from './musteri.page-object';
import MusteriUpdatePage from './musteri-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Musteri e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let musteriUpdatePage: MusteriUpdatePage;
  let musteriComponentsPage: MusteriComponentsPage;
  let musteriDeleteDialog: MusteriDeleteDialog;

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

  it('should load Musteris', async () => {
    await navBarPage.getEntityPage('musteri');
    musteriComponentsPage = new MusteriComponentsPage();
    expect(await musteriComponentsPage.getTitle().getText()).to.match(/Musteris/);
  });

  it('should load create Musteri page', async () => {
    await musteriComponentsPage.clickOnCreateButton();
    musteriUpdatePage = new MusteriUpdatePage();
    expect(await musteriUpdatePage.getPageTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.musteri.home.createOrEditLabel/);
  });

  it('should create and save Musteris', async () => {
    const nbButtonsBeforeCreate = await musteriComponentsPage.countDeleteButtons();

    await musteriUpdatePage.setAdInput('ad');
    expect(await musteriUpdatePage.getAdInput()).to.match(/ad/);
    await musteriUpdatePage.setSoyadInput('soyad');
    expect(await musteriUpdatePage.getSoyadInput()).to.match(/soyad/);
    // musteriUpdatePage.veliSelectLastOption();
    await waitUntilDisplayed(musteriUpdatePage.getSaveButton());
    await musteriUpdatePage.save();
    await waitUntilHidden(musteriUpdatePage.getSaveButton());
    expect(await musteriUpdatePage.getSaveButton().isPresent()).to.be.false;

    await musteriComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await musteriComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Musteri', async () => {
    await musteriComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await musteriComponentsPage.countDeleteButtons();
    await musteriComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    musteriDeleteDialog = new MusteriDeleteDialog();
    expect(await musteriDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.musteri.delete.question/);
    await musteriDeleteDialog.clickOnConfirmButton();

    await musteriComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await musteriComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
