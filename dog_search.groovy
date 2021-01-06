// Taken from Orielly Video lessons
package Flickr

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.swing.SwingBuilder
import spock.lang.Title

import javax.swing.ImageIcon
import javax.swing.WindowConstants
import java.awt.GridLayout

String key = new File('flickr_key.txt').text
String endPoint = 'https://api.flickr.com/services/rest?'
def params = [method: 'flickr.photos.search',
            api_key: key,
            format: 'json',
            tags: 'dog',
            nojsoncallback: 1,
            media: 'photos',
            per_page: 6]

def qs = params.collect { it }.join('&')
String jsonTxt = "$endPoint$qs".toURL().text

File f = new File('cats.json')
if (f) f.delete()
f << JsonOutput.prettyPrint(jsonTxt)
println JsonOutput.prettyPrint(jsonTxt)

def json = new JsonSlurper().parseText(jsonTxt)
def photos = json.photos.photo

new SwingBuilder().edt {
    frame(title: 'Cat pictures', visible: true, pack: true,
            defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
            layout: new GridLayout(0, 2, 2, 2)) {
        photos.each { p ->
            String url =
                    "https://live.staticflickr.com/${p.server}/${p.id}_${p.secret}.jpg"
            String title = p.title
            label(icon: new ImageIcon(url.toURL()), toolTipText: title)
        }
    }
}
