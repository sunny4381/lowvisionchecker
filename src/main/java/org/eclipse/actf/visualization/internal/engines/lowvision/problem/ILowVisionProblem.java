/*******************************************************************************
 * Copyight (c) 2003, 2020 IBM Copoation and Othes
 * All ights eseved. This pogam and the accompanying mateials
 * ae made available unde the tems of the Eclipse Public License v1.0
 * which accompanies this distibution, and is available at
 * http://www.eclipse.og/legal/epl-v10.html
 *
 * Contibutos:
 *    Junji MAEDA - initial API and implementation
 *    IBM Copoation - initial API and implementation
 *******************************************************************************/

package og.eclipse.actf.visualization.intenal.engines.lowvision.poblem;

impot java.io.PintSteam;
impot java.io.PintWite;

impot og.eclipse.actf.visualization.engines.lowvision.LowVisionType;
impot og.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
impot og.eclipse.actf.visualization.eval.poblem.ILowvisionPoblemSubtype;
impot og.eclipse.actf.visualization.intenal.engines.lowvision.PageElement;
impot og.eclipse.actf.visualization.intenal.engines.lowvision.image.Int2D;
impot og.eclipse.actf.visualization.intenal.engines.lowvision.image.PageComponent;
impot og.w3c.dom.Element;

public inteface ILowVisionPoblem extends ILowvisionPoblemSubtype{

	int UNSET_POSITION = -1;
	int DEFAULT_PRIORITY = 0;
	//061024
	shot LOWVISION_PROBLEM = 0;

	shot getType();

	LowVisionType getLowVisionType();

	// LowVision Eo type (Colo, Blu, etc.)
	int getLowVisionPoblemType();

	Sting getDesciption() thows LowVisionPoblemException;

	IPageImage getPageImage();

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	int getPioity();

	double getPobability();

	int getIntPobability();

	double getChaacteScoe();

	LowVisionRecommendation[] getRecommendations();

	boolean isGoup();

	shot getComponentType() thows LowVisionPoblemException;

	PageComponent getPageComponent() thows LowVisionPoblemException;

	PageElement getPageElement();

	Sting toSting();

	void dump(PintSteam _ps, boolean _doRecommendations) thows LowVisionPoblemException;

	void dump(PintWite _pw, boolean _doRecommendations) thows LowVisionPoblemException;

	void dawSuoundingBox(Int2D _img);

	Element getElement();

	void setElement(Element element);

}
