package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.Vector2f;
import com.volmit.iris.util.math.Vector3f;
import com.volmit.iris.util.stream.ProceduralStream;

public class FastNoise {
   private static final FastNoise.Float2[] GRAD_2D = new FastNoise.Float2[]{new FastNoise.Float2(-1.0F, -1.0F), new FastNoise.Float2(1.0F, -1.0F), new FastNoise.Float2(-1.0F, 1.0F), new FastNoise.Float2(1.0F, 1.0F), new FastNoise.Float2(0.0F, -1.0F), new FastNoise.Float2(-1.0F, 0.0F), new FastNoise.Float2(0.0F, 1.0F), new FastNoise.Float2(1.0F, 0.0F)};
   private static final FastNoise.Float3[] GRAD_3D = new FastNoise.Float3[]{new FastNoise.Float3(1.0F, 1.0F, 0.0F), new FastNoise.Float3(-1.0F, 1.0F, 0.0F), new FastNoise.Float3(1.0F, -1.0F, 0.0F), new FastNoise.Float3(-1.0F, -1.0F, 0.0F), new FastNoise.Float3(1.0F, 0.0F, 1.0F), new FastNoise.Float3(-1.0F, 0.0F, 1.0F), new FastNoise.Float3(1.0F, 0.0F, -1.0F), new FastNoise.Float3(-1.0F, 0.0F, -1.0F), new FastNoise.Float3(0.0F, 1.0F, 1.0F), new FastNoise.Float3(0.0F, -1.0F, 1.0F), new FastNoise.Float3(0.0F, 1.0F, -1.0F), new FastNoise.Float3(0.0F, -1.0F, -1.0F), new FastNoise.Float3(1.0F, 1.0F, 0.0F), new FastNoise.Float3(0.0F, -1.0F, 1.0F), new FastNoise.Float3(-1.0F, 1.0F, 0.0F), new FastNoise.Float3(0.0F, -1.0F, -1.0F)};
   private static final FastNoise.Float2[] CELL_2D = new FastNoise.Float2[]{new FastNoise.Float2(-0.43135393F, 0.12819435F), new FastNoise.Float2(-0.17333168F, 0.41527838F), new FastNoise.Float2(-0.28219575F, -0.35052183F), new FastNoise.Float2(-0.28064737F, 0.35176277F), new FastNoise.Float2(0.3125509F, -0.3237467F), new FastNoise.Float2(0.33830184F, -0.29673535F), new FastNoise.Float2(-0.4393982F, -0.09710417F), new FastNoise.Float2(-0.44604436F, -0.05953503F), new FastNoise.Float2(-0.30222303F, 0.3334085F), new FastNoise.Float2(-0.21268106F, -0.39656875F), new FastNoise.Float2(-0.29911566F, 0.33619907F), new FastNoise.Float2(0.22933237F, 0.38717782F), new FastNoise.Float2(0.44754392F, -0.046951506F), new FastNoise.Float2(0.1777518F, 0.41340572F), new FastNoise.Float2(0.16885225F, -0.4171198F), new FastNoise.Float2(-0.097659715F, 0.43927506F), new FastNoise.Float2(0.084501885F, 0.44199485F), new FastNoise.Float2(-0.40987605F, -0.18574613F), new FastNoise.Float2(0.34765857F, -0.2857158F), new FastNoise.Float2(-0.335067F, -0.30038327F), new FastNoise.Float2(0.229819F, -0.38688916F), new FastNoise.Float2(-0.010699241F, 0.4498728F), new FastNoise.Float2(-0.44601414F, -0.059761196F), new FastNoise.Float2(0.3650294F, 0.26316068F), new FastNoise.Float2(-0.34947944F, 0.28348568F), new FastNoise.Float2(-0.41227207F, 0.18036559F), new FastNoise.Float2(-0.26732782F, 0.36198872F), new FastNoise.Float2(0.32212403F, -0.31422302F), new FastNoise.Float2(0.2880446F, -0.34573156F), new FastNoise.Float2(0.38921708F, -0.22585405F), new FastNoise.Float2(0.4492085F, -0.026678115F), new FastNoise.Float2(-0.44977248F, 0.014307996F), new FastNoise.Float2(0.12781754F, -0.43146574F), new FastNoise.Float2(-0.035721004F, 0.44858F), new FastNoise.Float2(-0.4297407F, -0.13350253F), new FastNoise.Float2(-0.32178178F, 0.3145735F), new FastNoise.Float2(-0.3057159F, 0.33020872F), new FastNoise.Float2(-0.414504F, 0.17517549F), new FastNoise.Float2(-0.373814F, 0.25052565F), new FastNoise.Float2(0.22368914F, -0.39046532F), new FastNoise.Float2(0.0029677756F, -0.4499902F), new FastNoise.Float2(0.17471284F, -0.4146992F), new FastNoise.Float2(-0.44237724F, -0.08247648F), new FastNoise.Float2(-0.2763961F, -0.35511294F), new FastNoise.Float2(-0.4019386F, -0.20234962F), new FastNoise.Float2(0.3871414F, -0.22939382F), new FastNoise.Float2(-0.43000874F, 0.1326367F), new FastNoise.Float2(-0.030375743F, -0.44897363F), new FastNoise.Float2(-0.34861815F, 0.28454417F), new FastNoise.Float2(0.045535173F, -0.44769025F), new FastNoise.Float2(-0.037580293F, 0.44842806F), new FastNoise.Float2(0.3266409F, 0.309525F), new FastNoise.Float2(0.065400176F, -0.4452222F), new FastNoise.Float2(0.03409026F, 0.44870687F), new FastNoise.Float2(-0.44491938F, 0.06742967F), new FastNoise.Float2(-0.4255936F, -0.14618507F), new FastNoise.Float2(0.4499173F, 0.008627303F), new FastNoise.Float2(0.052426063F, 0.44693568F), new FastNoise.Float2(-0.4495305F, -0.020550266F), new FastNoise.Float2(-0.12047757F, 0.43357256F), new FastNoise.Float2(-0.3419864F, -0.2924813F), new FastNoise.Float2(0.386532F, 0.23041917F), new FastNoise.Float2(0.045060977F, -0.4477382F), new FastNoise.Float2(-0.06283466F, 0.4455915F), new FastNoise.Float2(0.39326003F, -0.21873853F), new FastNoise.Float2(0.44722617F, -0.04988731F), new FastNoise.Float2(0.3753571F, -0.24820767F), new FastNoise.Float2(-0.2736623F, 0.35722396F), new FastNoise.Float2(0.17004615F, 0.4166345F), new FastNoise.Float2(0.41026923F, 0.18487608F), new FastNoise.Float2(0.3232272F, -0.31308815F), new FastNoise.Float2(-0.28823102F, -0.34557614F), new FastNoise.Float2(0.20509727F, 0.4005435F), new FastNoise.Float2(0.4414086F, -0.08751257F), new FastNoise.Float2(-0.16847004F, 0.4172743F), new FastNoise.Float2(-0.0039780326F, 0.4499824F), new FastNoise.Float2(-0.20551336F, 0.4003302F), new FastNoise.Float2(-0.006095675F, -0.4499587F), new FastNoise.Float2(-0.11962281F, -0.43380916F), new FastNoise.Float2(0.39015284F, -0.2242337F), new FastNoise.Float2(0.017235318F, 0.4496698F), new FastNoise.Float2(-0.30150703F, 0.33405614F), new FastNoise.Float2(-0.015142624F, -0.44974515F), new FastNoise.Float2(-0.4142574F, -0.1757578F), new FastNoise.Float2(-0.19163772F, -0.40715474F), new FastNoise.Float2(0.37492487F, 0.24886008F), new FastNoise.Float2(-0.22377743F, 0.39041474F), new FastNoise.Float2(-0.41663432F, -0.17004661F), new FastNoise.Float2(0.36191717F, 0.2674247F), new FastNoise.Float2(0.18911268F, -0.4083337F), new FastNoise.Float2(-0.3127425F, 0.3235616F), new FastNoise.Float2(-0.3281808F, 0.30789182F), new FastNoise.Float2(-0.22948067F, 0.38708994F), new FastNoise.Float2(-0.34452662F, 0.28948474F), new FastNoise.Float2(-0.41670954F, -0.16986217F), new FastNoise.Float2(-0.2578903F, -0.36877173F), new FastNoise.Float2(-0.3612038F, 0.26838747F), new FastNoise.Float2(0.22679965F, 0.38866684F), new FastNoise.Float2(0.20715706F, 0.3994821F), new FastNoise.Float2(0.083551764F, -0.44217542F), new FastNoise.Float2(-0.43122333F, 0.12863296F), new FastNoise.Float2(0.32570556F, 0.3105091F), new FastNoise.Float2(0.1777011F, -0.41342753F), new FastNoise.Float2(-0.44518253F, 0.0656698F), new FastNoise.Float2(0.39551434F, 0.21463552F), new FastNoise.Float2(-0.4264614F, 0.14363383F), new FastNoise.Float2(-0.37937996F, -0.24201414F), new FastNoise.Float2(0.04617599F, -0.4476246F), new FastNoise.Float2(-0.37140542F, -0.25408268F), new FastNoise.Float2(0.25635704F, -0.36983925F), new FastNoise.Float2(0.03476646F, 0.44865498F), new FastNoise.Float2(-0.30654544F, 0.32943875F), new FastNoise.Float2(-0.22569798F, 0.38930762F), new FastNoise.Float2(0.41164485F, -0.18179253F), new FastNoise.Float2(-0.29077458F, -0.3434387F), new FastNoise.Float2(0.28422785F, -0.3488761F), new FastNoise.Float2(0.31145895F, -0.32479736F), new FastNoise.Float2(0.44641557F, -0.05668443F), new FastNoise.Float2(-0.3037334F, -0.33203316F), new FastNoise.Float2(0.4079607F, 0.18991591F), new FastNoise.Float2(-0.3486949F, -0.2844501F), new FastNoise.Float2(0.32648215F, 0.30969244F), new FastNoise.Float2(0.32111424F, 0.3152549F), new FastNoise.Float2(0.011833827F, 0.44984436F), new FastNoise.Float2(0.43338442F, 0.1211526F), new FastNoise.Float2(0.31186685F, 0.32440573F), new FastNoise.Float2(-0.27275348F, 0.35791835F), new FastNoise.Float2(-0.42222863F, -0.15563737F), new FastNoise.Float2(-0.10097001F, -0.438526F), new FastNoise.Float2(-0.2741171F, -0.35687506F), new FastNoise.Float2(-0.14651251F, 0.425481F), new FastNoise.Float2(0.2302279F, -0.38664597F), new FastNoise.Float2(-0.36994356F, 0.25620648F), new FastNoise.Float2(0.10570035F, -0.4374099F), new FastNoise.Float2(-0.26467136F, 0.36393553F), new FastNoise.Float2(0.3521828F, 0.2801201F), new FastNoise.Float2(-0.18641879F, -0.40957054F), new FastNoise.Float2(0.1994493F, -0.40338564F), new FastNoise.Float2(0.3937065F, 0.21793391F), new FastNoise.Float2(-0.32261583F, 0.31371805F), new FastNoise.Float2(0.37962353F, 0.2416319F), new FastNoise.Float2(0.1482922F, 0.424864F), new FastNoise.Float2(-0.4074004F, 0.19111493F), new FastNoise.Float2(0.4212853F, 0.15817298F), new FastNoise.Float2(-0.26212972F, 0.36577043F), new FastNoise.Float2(-0.2536987F, -0.37166783F), new FastNoise.Float2(-0.21002364F, 0.3979825F), new FastNoise.Float2(0.36241525F, 0.2667493F), new FastNoise.Float2(-0.36450386F, -0.26388812F), new FastNoise.Float2(0.23184867F, 0.38567626F), new FastNoise.Float2(-0.3260457F, 0.3101519F), new FastNoise.Float2(-0.21300453F, -0.3963951F), new FastNoise.Float2(0.3814999F, -0.23865843F), new FastNoise.Float2(-0.34297732F, 0.29131868F), new FastNoise.Float2(-0.43558657F, 0.11297941F), new FastNoise.Float2(-0.21046796F, 0.3977477F), new FastNoise.Float2(0.33483645F, -0.30064023F), new FastNoise.Float2(0.34304687F, 0.29123673F), new FastNoise.Float2(-0.22918367F, -0.38726586F), new FastNoise.Float2(0.25477073F, -0.3709338F), new FastNoise.Float2(0.42361748F, -0.1518164F), new FastNoise.Float2(-0.15387742F, 0.4228732F), new FastNoise.Float2(-0.44074494F, 0.09079596F), new FastNoise.Float2(-0.06805276F, -0.4448245F), new FastNoise.Float2(0.44535172F, -0.06451237F), new FastNoise.Float2(0.25624645F, -0.36991587F), new FastNoise.Float2(0.32781982F, -0.30827612F), new FastNoise.Float2(-0.41227743F, -0.18035334F), new FastNoise.Float2(0.3354091F, -0.30000123F), new FastNoise.Float2(0.44663286F, -0.054946158F), new FastNoise.Float2(-0.16089533F, 0.42025313F), new FastNoise.Float2(-0.09463955F, 0.43993562F), new FastNoise.Float2(-0.026376883F, -0.4492263F), new FastNoise.Float2(0.44710281F, -0.050981198F), new FastNoise.Float2(-0.4365671F, 0.10912917F), new FastNoise.Float2(-0.39598587F, 0.21376434F), new FastNoise.Float2(-0.42400482F, -0.15073125F), new FastNoise.Float2(-0.38827947F, 0.22746222F), new FastNoise.Float2(-0.42836526F, -0.13785212F), new FastNoise.Float2(0.3303888F, 0.30552125F), new FastNoise.Float2(0.3321435F, -0.30361274F), new FastNoise.Float2(-0.41302106F, -0.17864382F), new FastNoise.Float2(0.084030606F, -0.44208467F), new FastNoise.Float2(-0.38228828F, 0.23739347F), new FastNoise.Float2(-0.37123957F, -0.25432497F), new FastNoise.Float2(0.4472364F, -0.049795635F), new FastNoise.Float2(-0.44665912F, 0.054732345F), new FastNoise.Float2(0.048627254F, -0.44736493F), new FastNoise.Float2(-0.42031014F, -0.16074637F), new FastNoise.Float2(0.22053608F, 0.3922548F), new FastNoise.Float2(-0.36249006F, 0.2666476F), new FastNoise.Float2(-0.40360868F, -0.19899757F), new FastNoise.Float2(0.21527278F, 0.39516786F), new FastNoise.Float2(-0.43593928F, -0.11161062F), new FastNoise.Float2(0.4178354F, 0.1670735F), new FastNoise.Float2(0.20076302F, 0.40273342F), new FastNoise.Float2(-0.07278067F, -0.4440754F), new FastNoise.Float2(0.36447486F, -0.26392817F), new FastNoise.Float2(-0.43174517F, 0.12687041F), new FastNoise.Float2(-0.29743645F, 0.33768559F), new FastNoise.Float2(-0.2998672F, 0.3355289F), new FastNoise.Float2(-0.26736742F, 0.3619595F), new FastNoise.Float2(0.28084233F, 0.35160714F), new FastNoise.Float2(0.34989464F, 0.28297302F), new FastNoise.Float2(-0.22296856F, 0.39087725F), new FastNoise.Float2(0.33058232F, 0.30531186F), new FastNoise.Float2(-0.24366812F, -0.37831977F), new FastNoise.Float2(-0.034027766F, 0.4487116F), new FastNoise.Float2(-0.31935883F, 0.31703302F), new FastNoise.Float2(0.44546336F, -0.063737005F), new FastNoise.Float2(0.44835043F, 0.03849544F), new FastNoise.Float2(-0.44273585F, -0.08052933F), new FastNoise.Float2(0.054522987F, 0.44668472F), new FastNoise.Float2(-0.28125608F, 0.35127628F), new FastNoise.Float2(0.12666969F, 0.43180412F), new FastNoise.Float2(-0.37359813F, 0.25084746F), new FastNoise.Float2(0.29597083F, -0.3389709F), new FastNoise.Float2(-0.37143773F, 0.25403547F), new FastNoise.Float2(-0.4044671F, -0.19724695F), new FastNoise.Float2(0.16361657F, -0.41920117F), new FastNoise.Float2(0.32891855F, -0.30710354F), new FastNoise.Float2(-0.2494825F, -0.374511F), new FastNoise.Float2(0.032831334F, 0.44880074F), new FastNoise.Float2(-0.16630606F, -0.41814148F), new FastNoise.Float2(-0.10683318F, 0.43713462F), new FastNoise.Float2(0.0644026F, -0.4453676F), new FastNoise.Float2(-0.4483231F, 0.03881238F), new FastNoise.Float2(-0.42137775F, -0.15792651F), new FastNoise.Float2(0.05097921F, -0.44710302F), new FastNoise.Float2(0.20505841F, -0.40056342F), new FastNoise.Float2(0.41780984F, -0.16713744F), new FastNoise.Float2(-0.35651895F, -0.27458012F), new FastNoise.Float2(0.44783983F, 0.04403978F), new FastNoise.Float2(-0.33999997F, -0.2947881F), new FastNoise.Float2(0.3767122F, 0.24614613F), new FastNoise.Float2(-0.31389344F, 0.32244518F), new FastNoise.Float2(-0.14620018F, -0.42558843F), new FastNoise.Float2(0.39702904F, -0.21182053F), new FastNoise.Float2(0.44591492F, -0.0604969F), new FastNoise.Float2(-0.41048893F, -0.18438771F), new FastNoise.Float2(0.1475104F, -0.4251361F), new FastNoise.Float2(0.0925803F, 0.44037357F), new FastNoise.Float2(-0.15896647F, -0.42098653F), new FastNoise.Float2(0.2482445F, 0.37533274F), new FastNoise.Float2(0.43836242F, -0.10167786F), new FastNoise.Float2(0.06242803F, 0.44564867F), new FastNoise.Float2(0.2846591F, -0.3485243F), new FastNoise.Float2(-0.34420276F, -0.28986976F), new FastNoise.Float2(0.11981889F, -0.43375504F), new FastNoise.Float2(-0.2435907F, 0.37836963F), new FastNoise.Float2(0.2958191F, -0.3391033F), new FastNoise.Float2(-0.1164008F, 0.43468478F), new FastNoise.Float2(0.12740372F, -0.4315881F), new FastNoise.Float2(0.3680473F, 0.2589231F), new FastNoise.Float2(0.2451437F, 0.3773653F), new FastNoise.Float2(-0.43145096F, 0.12786736F)};
   private static final FastNoise.Float3[] CELL_3D = new FastNoise.Float3[]{new FastNoise.Float3(0.14537874F, -0.41497818F, -0.09569818F), new FastNoise.Float3(-0.012428297F, -0.14579184F, -0.42554703F), new FastNoise.Float3(0.28779796F, -0.026064834F, -0.34495357F), new FastNoise.Float3(-0.07732987F, 0.23770943F, 0.37418488F), new FastNoise.Float3(0.11072059F, -0.3552302F, -0.25308585F), new FastNoise.Float3(0.27552092F, 0.26405212F, -0.23846321F), new FastNoise.Float3(0.29416895F, 0.15260646F, 0.30442718F), new FastNoise.Float3(0.4000921F, -0.20340563F, 0.0324415F), new FastNoise.Float3(-0.16973041F, 0.39708647F, -0.12654613F), new FastNoise.Float3(-0.14832245F, -0.38596946F, 0.17756131F), new FastNoise.Float3(0.2623597F, -0.2354853F, 0.27966776F), new FastNoise.Float3(-0.2709003F, 0.3505271F, -0.07901747F), new FastNoise.Float3(-0.035165507F, 0.38852343F, 0.22430544F), new FastNoise.Float3(-0.12677127F, 0.1920044F, 0.38673422F), new FastNoise.Float3(0.02952022F, 0.44096857F, 0.084706925F), new FastNoise.Float3(-0.28068542F, -0.26699677F, 0.22897254F), new FastNoise.Float3(-0.17115955F, 0.21411856F, 0.35687205F), new FastNoise.Float3(0.21132272F, 0.39024058F, -0.074531786F), new FastNoise.Float3(-0.10243528F, 0.21280442F, -0.38304216F), new FastNoise.Float3(-0.330425F, -0.15669867F, 0.26223055F), new FastNoise.Float3(0.20911114F, 0.31332782F, -0.24616706F), new FastNoise.Float3(0.34467816F, -0.19442405F, -0.21423413F), new FastNoise.Float3(0.19844781F, -0.32143423F, -0.24453732F), new FastNoise.Float3(-0.29290086F, 0.22629151F, 0.2559321F), new FastNoise.Float3(-0.16173328F, 0.00631477F, -0.41988388F), new FastNoise.Float3(-0.35820603F, -0.14830318F, -0.2284614F), new FastNoise.Float3(-0.18520673F, -0.34541193F, -0.2211087F), new FastNoise.Float3(0.3046301F, 0.10263104F, 0.3149085F), new FastNoise.Float3(-0.038167685F, -0.25517663F, -0.3686843F), new FastNoise.Float3(-0.40849522F, 0.18059509F, 0.05492789F), new FastNoise.Float3(-0.026874434F, -0.27497414F, 0.35519993F), new FastNoise.Float3(-0.038010985F, 0.3277859F, 0.30596006F), new FastNoise.Float3(0.23711208F, 0.29003868F, -0.2493099F), new FastNoise.Float3(0.44476604F, 0.039469305F, 0.05590469F), new FastNoise.Float3(0.019851472F, -0.015031833F, -0.44931054F), new FastNoise.Float3(0.4274339F, 0.033459943F, -0.1366773F), new FastNoise.Float3(-0.20729886F, 0.28714147F, -0.27762738F), new FastNoise.Float3(-0.3791241F, 0.12811777F, 0.205793F), new FastNoise.Float3(-0.20987213F, -0.10070873F, -0.38511226F), new FastNoise.Float3(0.01582799F, 0.42638946F, 0.14297384F), new FastNoise.Float3(-0.18881294F, -0.31609967F, -0.2587096F), new FastNoise.Float3(0.1612989F, -0.19748051F, -0.3707885F), new FastNoise.Float3(-0.08974491F, 0.22914875F, -0.37674487F), new FastNoise.Float3(0.07041229F, 0.41502303F, -0.15905343F), new FastNoise.Float3(-0.108292565F, -0.15860616F, 0.40696046F), new FastNoise.Float3(0.24741006F, -0.33094147F, 0.17823021F), new FastNoise.Float3(-0.10688367F, -0.27016446F, -0.34363797F), new FastNoise.Float3(0.23964521F, 0.068036005F, -0.37475494F), new FastNoise.Float3(-0.30638862F, 0.25974283F, 0.2028785F), new FastNoise.Float3(0.15933429F, -0.311435F, -0.2830562F), new FastNoise.Float3(0.27096906F, 0.14126487F, -0.33033317F), new FastNoise.Float3(-0.15197805F, 0.3623355F, 0.2193528F), new FastNoise.Float3(0.16997737F, 0.3456013F, 0.232739F), new FastNoise.Float3(-0.19861557F, 0.38362765F, -0.12602258F), new FastNoise.Float3(-0.18874821F, -0.2050155F, -0.35333094F), new FastNoise.Float3(0.26591033F, 0.3015631F, -0.20211722F), new FastNoise.Float3(-0.08838976F, -0.42888197F, -0.1036702F), new FastNoise.Float3(-0.042018693F, 0.30995926F, 0.3235115F), new FastNoise.Float3(-0.32303345F, 0.20154992F, -0.23984788F), new FastNoise.Float3(0.2612721F, 0.27598545F, -0.24097495F), new FastNoise.Float3(0.38571304F, 0.21934603F, 0.074918374F), new FastNoise.Float3(0.07654968F, 0.37217322F, 0.24109592F), new FastNoise.Float3(0.4317039F, -0.02577753F, 0.12436751F), new FastNoise.Float3(-0.28904364F, -0.341818F, -0.045980845F), new FastNoise.Float3(-0.22019476F, 0.38302338F, -0.085483104F), new FastNoise.Float3(0.41613227F, -0.16696343F, -0.03817252F), new FastNoise.Float3(0.22047181F, 0.02654239F, -0.391392F), new FastNoise.Float3(-0.10403074F, 0.38900796F, -0.2008741F), new FastNoise.Float3(-0.14321226F, 0.3716144F, -0.20950656F), new FastNoise.Float3(0.39783806F, -0.062066693F, 0.20092937F), new FastNoise.Float3(-0.25992745F, 0.2616725F, -0.25780848F), new FastNoise.Float3(0.40326184F, -0.11245936F, 0.1650236F), new FastNoise.Float3(-0.0895347F, -0.30482447F, 0.31869355F), new FastNoise.Float3(0.1189372F, -0.2875222F, 0.3250922F), new FastNoise.Float3(0.02167047F, -0.032846306F, -0.44827616F), new FastNoise.Float3(-0.34113437F, 0.2500031F, 0.15370683F), new FastNoise.Float3(0.31629646F, 0.3082064F, -0.08640228F), new FastNoise.Float3(0.2355139F, -0.34393343F, -0.16953762F), new FastNoise.Float3(-0.028745415F, -0.39559332F, 0.21255504F), new FastNoise.Float3(-0.24614552F, 0.020202823F, -0.3761705F), new FastNoise.Float3(0.042080294F, -0.44704396F, 0.029680781F), new FastNoise.Float3(0.27274588F, 0.22884719F, -0.27520657F), new FastNoise.Float3(-0.13475229F, -0.027208483F, -0.42848748F), new FastNoise.Float3(0.38296244F, 0.123193145F, -0.20165123F), new FastNoise.Float3(-0.35476136F, 0.12717022F, 0.24591078F), new FastNoise.Float3(0.23057902F, 0.30638957F, 0.23549682F), new FastNoise.Float3(-0.08323845F, -0.19222452F, 0.39827263F), new FastNoise.Float3(0.2993663F, -0.2619918F, -0.21033332F), new FastNoise.Float3(-0.21548657F, 0.27067477F, 0.2877511F), new FastNoise.Float3(0.016833553F, -0.26806557F, -0.36105052F), new FastNoise.Float3(0.052404292F, 0.4335128F, -0.108721785F), new FastNoise.Float3(0.0094010485F, -0.44728905F, 0.0484161F), new FastNoise.Float3(0.34656888F, 0.011419145F, -0.28680938F), new FastNoise.Float3(-0.3706868F, -0.25511044F, 0.0031566927F), new FastNoise.Float3(0.274117F, 0.21399724F, -0.28559598F), new FastNoise.Float3(0.06413434F, 0.17087185F, 0.41132662F), new FastNoise.Float3(-0.38818797F, -0.039732803F, -0.22412363F), new FastNoise.Float3(0.064194694F, -0.28036824F, 0.3460819F), new FastNoise.Float3(-0.19861208F, -0.33911735F, 0.21920918F), new FastNoise.Float3(-0.20320301F, -0.38716415F, 0.10636004F), new FastNoise.Float3(-0.13897364F, -0.27759016F, -0.32577604F), new FastNoise.Float3(-0.065556414F, 0.34225327F, -0.28471926F), new FastNoise.Float3(-0.25292465F, -0.2904228F, 0.23277397F), new FastNoise.Float3(0.14444765F, 0.1069184F, 0.41255707F), new FastNoise.Float3(-0.364378F, -0.24471F, -0.09922543F), new FastNoise.Float3(0.42861426F, -0.13584961F, -0.018295068F), new FastNoise.Float3(0.16587292F, -0.31368086F, -0.27674988F), new FastNoise.Float3(0.22196105F, -0.365814F, 0.13933203F), new FastNoise.Float3(0.043229405F, -0.38327307F, 0.23180372F), new FastNoise.Float3(-0.0848127F, -0.44048697F, -0.035749655F), new FastNoise.Float3(0.18220821F, -0.39532593F, 0.1140946F), new FastNoise.Float3(-0.32693234F, 0.30365425F, 0.05838957F), new FastNoise.Float3(-0.40804854F, 0.042278584F, -0.18495652F), new FastNoise.Float3(0.26760253F, -0.012996716F, 0.36155218F), new FastNoise.Float3(0.30248925F, -0.10099903F, -0.3174893F), new FastNoise.Float3(0.1448494F, 0.42592168F, -0.01045808F), new FastNoise.Float3(0.41984022F, 0.0806232F, 0.14047809F), new FastNoise.Float3(-0.30088723F, -0.3330409F, -0.032413557F), new FastNoise.Float3(0.36393103F, -0.12912844F, -0.23104121F), new FastNoise.Float3(0.32958066F, 0.018417599F, -0.30583882F), new FastNoise.Float3(0.27762595F, -0.2974929F, -0.19215047F), new FastNoise.Float3(0.41490006F, -0.14479318F, -0.096916884F), new FastNoise.Float3(0.14501671F, -0.039899293F, 0.4241205F), new FastNoise.Float3(0.092990234F, -0.29973218F, -0.32251117F), new FastNoise.Float3(0.10289071F, -0.36126688F, 0.24778973F), new FastNoise.Float3(0.26830572F, -0.070760414F, -0.35426685F), new FastNoise.Float3(-0.4227307F, -0.07933162F, -0.13230732F), new FastNoise.Float3(-0.17812248F, 0.18068571F, -0.3716518F), new FastNoise.Float3(0.43907887F, -0.028418485F, -0.094351165F), new FastNoise.Float3(0.29725835F, 0.23827997F, -0.23949975F), new FastNoise.Float3(-0.17070028F, 0.22158457F, 0.3525077F), new FastNoise.Float3(0.38066867F, 0.14718525F, -0.18954648F), new FastNoise.Float3(-0.17514457F, -0.2748879F, 0.31025964F), new FastNoise.Float3(-0.22272375F, -0.23167789F, 0.31499124F), new FastNoise.Float3(0.13696331F, 0.13413431F, -0.40712288F), new FastNoise.Float3(-0.35295033F, -0.24728934F, -0.1295146F), new FastNoise.Float3(-0.25907442F, -0.29855776F, -0.21504351F), new FastNoise.Float3(-0.37840194F, 0.21998167F, -0.10449899F), new FastNoise.Float3(-0.056358058F, 0.14857374F, 0.42101023F), new FastNoise.Float3(0.32514286F, 0.09666047F, -0.29570064F), new FastNoise.Float3(-0.41909957F, 0.14067514F, -0.08405979F), new FastNoise.Float3(-0.3253151F, -0.3080335F, -0.042254567F), new FastNoise.Float3(0.2857946F, -0.05796152F, 0.34272718F), new FastNoise.Float3(-0.2733604F, 0.1973771F, -0.29802075F), new FastNoise.Float3(0.21900366F, 0.24100378F, -0.31057137F), new FastNoise.Float3(0.31827673F, -0.27134296F, 0.16605099F), new FastNoise.Float3(-0.03222023F, -0.33311614F, -0.30082467F), new FastNoise.Float3(-0.30877802F, 0.19927941F, -0.25969952F), new FastNoise.Float3(-0.06487612F, -0.4311323F, 0.11142734F), new FastNoise.Float3(0.39211714F, -0.06294284F, -0.2116184F), new FastNoise.Float3(-0.16064045F, -0.3589281F, -0.21878128F), new FastNoise.Float3(-0.037677713F, -0.22903514F, 0.3855169F), new FastNoise.Float3(0.13948669F, -0.3602214F, 0.23083329F), new FastNoise.Float3(-0.4345094F, 0.005751117F, 0.11691243F), new FastNoise.Float3(-0.10446375F, 0.41681284F, -0.13362028F), new FastNoise.Float3(0.26587275F, 0.25519434F, 0.2582393F), new FastNoise.Float3(0.2051462F, 0.19753908F, 0.3484155F), new FastNoise.Float3(-0.26608557F, 0.23483312F, 0.2766801F), new FastNoise.Float3(0.07849406F, -0.33003464F, -0.29566166F), new FastNoise.Float3(-0.21606864F, 0.053764515F, -0.39105463F), new FastNoise.Float3(-0.18577918F, 0.21484992F, 0.34903526F), new FastNoise.Float3(0.024924217F, -0.32299542F, -0.31233433F), new FastNoise.Float3(-0.12016783F, 0.40172666F, 0.16332598F), new FastNoise.Float3(-0.021600846F, -0.06885389F, 0.44417626F), new FastNoise.Float3(0.259767F, 0.30963007F, 0.19786438F), new FastNoise.Float3(-0.16115539F, -0.09823036F, 0.40850917F), new FastNoise.Float3(-0.32788968F, 0.14616702F, 0.27133662F), new FastNoise.Float3(0.2822735F, 0.03754421F, -0.3484424F), new FastNoise.Float3(0.03169341F, 0.34740525F, -0.28426242F), new FastNoise.Float3(0.22026137F, -0.3460788F, -0.18497133F), new FastNoise.Float3(0.2933396F, 0.30319735F, 0.15659896F), new FastNoise.Float3(-0.3194923F, 0.24537522F, -0.20053846F), new FastNoise.Float3(-0.3441586F, -0.16988562F, -0.23493347F), new FastNoise.Float3(0.27036458F, -0.35742772F, 0.040600598F), new FastNoise.Float3(0.2298569F, 0.37441564F, 0.09735889F), new FastNoise.Float3(0.09326604F, -0.31701088F, 0.30545956F), new FastNoise.Float3(-0.11161653F, -0.29850188F, 0.31770802F), new FastNoise.Float3(0.21729073F, -0.34600052F, -0.1885958F), new FastNoise.Float3(0.19913395F, 0.38203415F, -0.12998295F), new FastNoise.Float3(-0.054191817F, -0.21031451F, 0.3941206F), new FastNoise.Float3(0.08871337F, 0.20121174F, 0.39261147F), new FastNoise.Float3(0.27876732F, 0.35054046F, 0.04370535F), new FastNoise.Float3(-0.32216644F, 0.30672136F, 0.06804997F), new FastNoise.Float3(-0.42773664F, 0.13206677F, 0.045822866F), new FastNoise.Float3(0.24013188F, -0.1612516F, 0.34472394F), new FastNoise.Float3(0.1448608F, -0.2387819F, 0.35284352F), new FastNoise.Float3(-0.38370657F, -0.22063984F, 0.081162356F), new FastNoise.Float3(-0.4382628F, -0.09082753F, -0.046648555F), new FastNoise.Float3(-0.37728354F, 0.05445141F, 0.23914887F), new FastNoise.Float3(0.12595794F, 0.34839457F, 0.25545222F), new FastNoise.Float3(-0.14062855F, -0.27087736F, -0.33067968F), new FastNoise.Float3(-0.15806945F, 0.4162932F, -0.06491554F), new FastNoise.Float3(0.2477612F, -0.29278675F, -0.23535146F), new FastNoise.Float3(0.29161328F, 0.33125353F, 0.08793625F), new FastNoise.Float3(0.073652655F, -0.16661598F, 0.4114783F), new FastNoise.Float3(-0.26126525F, -0.24222377F, 0.27489653F), new FastNoise.Float3(-0.3721862F, 0.25279015F, 0.008634938F), new FastNoise.Float3(-0.36911917F, -0.25528118F, 0.032902323F), new FastNoise.Float3(0.22784418F, -0.3358365F, 0.1944245F), new FastNoise.Float3(0.36339816F, -0.23101902F, 0.13065979F), new FastNoise.Float3(-0.3042315F, -0.26984522F, 0.19268309F), new FastNoise.Float3(-0.3199312F, 0.31633255F, -0.008816978F), new FastNoise.Float3(0.28748524F, 0.16422755F, -0.30476475F), new FastNoise.Float3(-0.14510968F, 0.3277541F, -0.27206695F), new FastNoise.Float3(0.3220091F, 0.05113441F, 0.31015387F), new FastNoise.Float3(-0.12474009F, -0.043336052F, -0.4301882F), new FastNoise.Float3(-0.2829556F, -0.30561906F, -0.1703911F), new FastNoise.Float3(0.10693844F, 0.34910247F, -0.26304305F), new FastNoise.Float3(-0.14206612F, -0.30553767F, -0.29826826F), new FastNoise.Float3(-0.25054833F, 0.31564668F, -0.20023163F), new FastNoise.Float3(0.3265788F, 0.18712291F, 0.24664004F), new FastNoise.Float3(0.07646097F, -0.30266908F, 0.3241067F), new FastNoise.Float3(0.34517714F, 0.27571207F, -0.085648015F), new FastNoise.Float3(0.29813796F, 0.2852657F, 0.17954728F), new FastNoise.Float3(0.28122503F, 0.34667164F, 0.056844097F), new FastNoise.Float3(0.43903455F, -0.0979043F, -0.012783354F), new FastNoise.Float3(0.21483733F, 0.18501726F, 0.3494475F), new FastNoise.Float3(0.2595421F, -0.07946825F, 0.3589188F), new FastNoise.Float3(0.3182823F, -0.30735552F, -0.08203022F), new FastNoise.Float3(-0.40898594F, -0.046477184F, 0.18185264F), new FastNoise.Float3(-0.2826749F, 0.07417482F, 0.34218854F), new FastNoise.Float3(0.34838647F, 0.22544225F, -0.1740766F), new FastNoise.Float3(-0.32264152F, -0.14205854F, -0.27968165F), new FastNoise.Float3(0.4330735F, -0.11886856F, -0.028594075F), new FastNoise.Float3(-0.08717822F, -0.39098963F, -0.20500502F), new FastNoise.Float3(-0.21496783F, 0.3939974F, -0.032478984F), new FastNoise.Float3(-0.26873308F, 0.32268628F, -0.16172849F), new FastNoise.Float3(0.2105665F, -0.1961317F, -0.34596834F), new FastNoise.Float3(0.43618459F, -0.11055175F, 0.0046166084F), new FastNoise.Float3(0.053333335F, -0.3136395F, -0.31825432F), new FastNoise.Float3(-0.059862167F, 0.13610291F, -0.4247264F), new FastNoise.Float3(0.36649886F, 0.2550543F, -0.055909745F), new FastNoise.Float3(-0.23410155F, -0.18240573F, 0.33826706F), new FastNoise.Float3(-0.047309477F, -0.422215F, -0.14831145F), new FastNoise.Float3(-0.23915662F, -0.25776964F, -0.28081828F), new FastNoise.Float3(-0.1242081F, 0.42569533F, -0.07652336F), new FastNoise.Float3(0.26148328F, -0.36501792F, 0.02980623F), new FastNoise.Float3(-0.27287948F, -0.3499629F, 0.07458405F), new FastNoise.Float3(0.0078929F, -0.16727713F, 0.41767937F), new FastNoise.Float3(-0.017303303F, 0.29784867F, -0.33687797F), new FastNoise.Float3(0.20548357F, -0.32526004F, -0.23341466F), new FastNoise.Float3(-0.3231995F, 0.15642828F, -0.2712421F), new FastNoise.Float3(-0.2669546F, 0.25993437F, -0.2523279F), new FastNoise.Float3(-0.05554373F, 0.3170814F, -0.3144428F), new FastNoise.Float3(-0.20839357F, -0.31092283F, -0.24979813F), new FastNoise.Float3(0.06989323F, -0.31561416F, 0.31305373F), new FastNoise.Float3(0.38475662F, -0.16053091F, -0.16938764F), new FastNoise.Float3(-0.30262154F, -0.30015376F, -0.14431883F), new FastNoise.Float3(0.34507355F, 0.0861152F, 0.27569625F), new FastNoise.Float3(0.18144733F, -0.27887824F, -0.3029914F), new FastNoise.Float3(-0.038550105F, 0.09795111F, 0.4375151F), new FastNoise.Float3(0.35336703F, 0.26657528F, 0.08105161F), new FastNoise.Float3(-0.007945601F, 0.14035943F, -0.42747644F), new FastNoise.Float3(0.40630993F, -0.14917682F, -0.123119935F), new FastNoise.Float3(-0.20167735F, 0.008816271F, -0.40217972F), new FastNoise.Float3(-0.075270556F, -0.42564347F, -0.12514779F)};
   private static final int X_PRIME = 1619;
   private static final int Y_PRIME = 31337;
   private static final int Z_PRIME = 6971;
   private static final int W_PRIME = 1013;
   private static final float F3 = 0.33333334F;
   private static final float G3 = 0.16666667F;
   private static final float G33 = -0.5F;
   private static final float SQRT3 = 1.7320508F;
   private static final float F2 = 0.3660254F;
   private static final float G2 = 0.21132487F;
   private static final byte[] SIMPLEX_4D = new byte[]{0, 1, 2, 3, 0, 1, 3, 2, 0, 0, 0, 0, 0, 2, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0, 0, 2, 1, 3, 0, 0, 0, 0, 0, 3, 1, 2, 0, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 3, 0, 0, 0, 0, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 1, 2, 3, 1, 0, 1, 0, 2, 3, 1, 0, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 0, 0, 0, 0, 2, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1, 2, 3, 0, 2, 1, 0, 0, 0, 0, 3, 1, 2, 0, 2, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 2, 0, 0, 0, 0, 3, 2, 0, 1, 3, 2, 1, 0};
   private static final float F4 = 0.309017F;
   private static final float G4 = 0.1381966F;
   private static final float CUBIC_3D_BOUNDING = 0.2962963F;
   private static final float CUBIC_2D_BOUNDING = 0.44444445F;
   private int m_seed;
   private float m_frequency;
   private FastNoise.Interp m_interp;
   private FastNoise.NoiseType m_noiseType;
   private int m_octaves;
   private float m_lacunarity;
   private float m_gain;
   private FastNoise.FractalType m_fractalType;
   private float m_fractalBounding;
   private FastNoise.CellularDistanceFunction m_cellularDistanceFunction;
   private FastNoise.CellularReturnType m_cellularReturnType;
   private FastNoise m_cellularNoiseLookup;
   private float m_gradientPerturbAmp;

   public FastNoise() {
      this(1337);
   }

   public FastNoise(int seed) {
      this.m_seed = 1337;
      this.m_frequency = 0.01F;
      this.m_interp = FastNoise.Interp.Quintic;
      this.m_noiseType = FastNoise.NoiseType.Simplex;
      this.m_octaves = 3;
      this.m_lacunarity = 2.0F;
      this.m_gain = 0.5F;
      this.m_fractalType = FastNoise.FractalType.FBM;
      this.m_cellularDistanceFunction = FastNoise.CellularDistanceFunction.Euclidean;
      this.m_cellularReturnType = FastNoise.CellularReturnType.CellValue;
      this.m_cellularNoiseLookup = null;
      this.m_gradientPerturbAmp = 2.2222223F;
      this.m_seed = var1;
      this.CalculateFractalBounding();
   }

   public static float GetDecimalType() {
      return 0.0F;
   }

   private static int FastFloor(float f) {
      return var0 >= 0.0F ? (int)var0 : (int)var0 - 1;
   }

   private static int FastRound(float f) {
      return var0 >= 0.0F ? (int)(var0 + 0.5F) : (int)(var0 - 0.5F);
   }

   private static float Lerp(float a, float b, float t) {
      return var0 + var2 * (var1 - var0);
   }

   private static float InterpHermiteFunc(float t) {
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   private static float InterpQuinticFunc(float t) {
      return var0 * var0 * var0 * (var0 * (var0 * 6.0F - 15.0F) + 10.0F);
   }

   private static float CubicLerp(float a, float b, float c, float d, float t) {
      float var5 = var3 - var2 - (var0 - var1);
      return var4 * var4 * var4 * var5 + var4 * var4 * (var0 - var1 - var5) + var4 * (var2 - var0) + var1;
   }

   private static int Hash2D(int seed, int x, int y) {
      int var3 = var0 ^ 1619 * var1;
      var3 ^= 31337 * var2;
      var3 = var3 * var3 * var3 * '\uec4d';
      var3 ^= var3 >> 13;
      return var3;
   }

   private static int Hash3D(int seed, int x, int y, int z) {
      int var4 = var0 ^ 1619 * var1;
      var4 ^= 31337 * var2;
      var4 ^= 6971 * var3;
      var4 = var4 * var4 * var4 * '\uec4d';
      var4 ^= var4 >> 13;
      return var4;
   }

   public static int Hash4D(int seed, int x, int y, int z, int w) {
      int var5 = var0 ^ 1619 * var1;
      var5 ^= 31337 * var2;
      var5 ^= 6971 * var3;
      var5 ^= 1013 * var4;
      var5 = var5 * var5 * var5 * '\uec4d';
      var5 ^= var5 >> 13;
      return var5;
   }

   private static float ValCoord2D(int seed, int x, int y) {
      int var3 = var0 ^ 1619 * var1;
      var3 ^= 31337 * var2;
      return (float)(var3 * var3 * var3 * '\uec4d') / 2.14748365E9F;
   }

   private static float ValCoord3D(int seed, int x, int y, int z) {
      int var4 = var0 ^ 1619 * var1;
      var4 ^= 31337 * var2;
      var4 ^= 6971 * var3;
      return (float)(var4 * var4 * var4 * '\uec4d') / 2.14748365E9F;
   }

   private static float ValCoord4D(int seed, int x, int y, int z, int w) {
      int var5 = var0 ^ 1619 * var1;
      var5 ^= 31337 * var2;
      var5 ^= 6971 * var3;
      var5 ^= 1013 * var4;
      return (float)(var5 * var5 * var5 * '\uec4d') / 2.14748365E9F;
   }

   private static float GradCoord2D(int seed, int x, int y, float xd, float yd) {
      int var5 = var0 ^ 1619 * var1;
      var5 ^= 31337 * var2;
      var5 = var5 * var5 * var5 * '\uec4d';
      var5 ^= var5 >> 13;
      FastNoise.Float2 var6 = GRAD_2D[var5 & 7];
      return var3 * var6.x + var4 * var6.y;
   }

   private static float GradCoord3D(int seed, int x, int y, int z, float xd, float yd, float zd) {
      int var7 = var0 ^ 1619 * var1;
      var7 ^= 31337 * var2;
      var7 ^= 6971 * var3;
      var7 = var7 * var7 * var7 * '\uec4d';
      var7 ^= var7 >> 13;
      FastNoise.Float3 var8 = GRAD_3D[var7 & 15];
      return var4 * var8.x + var5 * var8.y + var6 * var8.z;
   }

   private static float GradCoord4D(int seed, int x, int y, int z, int w, float xd, float yd, float zd, float wd) {
      int var9 = var0 ^ 1619 * var1;
      var9 ^= 31337 * var2;
      var9 ^= 6971 * var3;
      var9 ^= 1013 * var4;
      var9 = var9 * var9 * var9 * '\uec4d';
      var9 ^= var9 >> 13;
      var9 &= 31;
      float var10 = var6;
      float var11 = var7;
      float var12 = var8;
      switch(var9 >> 3) {
      case 1:
         var10 = var8;
         var11 = var5;
         var12 = var6;
         break;
      case 2:
         var10 = var7;
         var11 = var8;
         var12 = var5;
         break;
      case 3:
         var10 = var6;
         var11 = var7;
         var12 = var8;
      }

      return ((var9 & 4) == 0 ? -var10 : var10) + ((var9 & 2) == 0 ? -var11 : var11) + ((var9 & 1) == 0 ? -var12 : var12);
   }

   public int GetSeed() {
      return this.m_seed;
   }

   public void SetSeed(int seed) {
      this.m_seed = var1;
   }

   public void SetFrequency(float frequency) {
      this.m_frequency = var1;
   }

   public void SetInterp(FastNoise.Interp interp) {
      this.m_interp = var1;
   }

   public void SetNoiseType(FastNoise.NoiseType noiseType) {
      this.m_noiseType = var1;
   }

   public void SetFractalOctaves(int octaves) {
      this.m_octaves = var1;
      this.CalculateFractalBounding();
   }

   public void SetFractalLacunarity(float lacunarity) {
      this.m_lacunarity = var1;
   }

   public void SetFractalGain(float gain) {
      this.m_gain = var1;
      this.CalculateFractalBounding();
   }

   public void SetFractalType(FastNoise.FractalType fractalType) {
      this.m_fractalType = var1;
   }

   public void SetCellularDistanceFunction(FastNoise.CellularDistanceFunction cellularDistanceFunction) {
      this.m_cellularDistanceFunction = var1;
   }

   public void SetCellularReturnType(FastNoise.CellularReturnType cellularReturnType) {
      this.m_cellularReturnType = var1;
   }

   public void SetCellularNoiseLookup(FastNoise noise) {
      this.m_cellularNoiseLookup = var1;
   }

   public void SetGradientPerturbAmp(float gradientPerturbAmp) {
      this.m_gradientPerturbAmp = var1 / 0.45F;
   }

   private void CalculateFractalBounding() {
      float var1 = this.m_gain;
      float var2 = 1.0F;

      for(int var3 = 1; var3 < this.m_octaves; ++var3) {
         var2 += var1;
         var1 *= this.m_gain;
      }

      this.m_fractalBounding = 1.0F / var2;
   }

   public float GetNoise(float x, float y, float z) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      var3 *= this.m_frequency;
      switch(this.m_noiseType.ordinal()) {
      case 0:
         return this.SingleValue(this.m_seed, var1, var2, var3);
      case 1:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SingleValueFractalFBM(var1, var2, var3);
         case 1:
            return this.SingleValueFractalBillow(var1, var2, var3);
         case 2:
            return this.SingleValueFractalRigidMulti(var1, var2, var3);
         default:
            return 0.0F;
         }
      case 2:
         return this.SinglePerlin(this.m_seed, var1, var2, var3);
      case 3:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SinglePerlinFractalFBM(var1, var2, var3);
         case 1:
            return this.SinglePerlinFractalBillow(var1, var2, var3);
         case 2:
            return this.SinglePerlinFractalRigidMulti(var1, var2, var3);
         default:
            return 0.0F;
         }
      case 4:
         return this.SingleSimplex(this.m_seed, var1, var2, var3);
      case 5:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SingleSimplexFractalFBM(var1, var2, var3);
         case 1:
            return this.SingleSimplexFractalBillow(var1, var2, var3);
         case 2:
            return this.SingleSimplexFractalRigidMulti(var1, var2, var3);
         default:
            return 0.0F;
         }
      case 6:
         switch(this.m_cellularReturnType.ordinal()) {
         case 0:
         case 1:
         case 2:
            return this.SingleCellular(var1, var2, var3);
         default:
            return this.SingleCellular2Edge(var1, var2, var3);
         }
      case 7:
         return this.GetWhiteNoise((double)var1, (double)var2, (double)var3);
      case 8:
         return this.SingleCubic(this.m_seed, var1, var2, var3);
      case 9:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SingleCubicFractalFBM(var1, var2, var3);
         case 1:
            return this.SingleCubicFractalBillow(var1, var2, var3);
         case 2:
            return this.SingleCubicFractalRigidMulti(var1, var2, var3);
         default:
            return 0.0F;
         }
      default:
         return 0.0F;
      }
   }

   public float GetNoise(float x, float y) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      switch(this.m_noiseType.ordinal()) {
      case 0:
         return this.SingleValue(this.m_seed, var1, var2);
      case 1:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SingleValueFractalFBM(var1, var2);
         case 1:
            return this.SingleValueFractalBillow(var1, var2);
         case 2:
            return this.SingleValueFractalRigidMulti(var1, var2);
         default:
            return 0.0F;
         }
      case 2:
         return this.SinglePerlin(this.m_seed, var1, var2);
      case 3:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SinglePerlinFractalFBM(var1, var2);
         case 1:
            return this.SinglePerlinFractalBillow(var1, var2);
         case 2:
            return this.SinglePerlinFractalRigidMulti(var1, var2);
         default:
            return 0.0F;
         }
      case 4:
         return this.SingleSimplex(this.m_seed, var1, var2);
      case 5:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SingleSimplexFractalFBM(var1, var2);
         case 1:
            return this.SingleSimplexFractalBillow(var1, var2);
         case 2:
            return this.SingleSimplexFractalRigidMulti(var1, var2);
         default:
            return 0.0F;
         }
      case 6:
         switch(this.m_cellularReturnType.ordinal()) {
         case 0:
         case 1:
         case 2:
            return this.SingleCellular(var1, var2);
         default:
            return this.SingleCellular2Edge(var1, var2);
         }
      case 7:
         return this.GetWhiteNoise((double)var1, (double)var2);
      case 8:
         return this.SingleCubic(this.m_seed, var1, var2);
      case 9:
         switch(this.m_fractalType.ordinal()) {
         case 0:
            return this.SingleCubicFractalFBM(var1, var2);
         case 1:
            return this.SingleCubicFractalBillow(var1, var2);
         case 2:
            return this.SingleCubicFractalRigidMulti(var1, var2);
         default:
            return 0.0F;
         }
      default:
         return 0.0F;
      }
   }

   private int FloatCast2Int(float f) {
      int var2 = Float.floatToRawIntBits(var1);
      return var2 ^ var2 >> 16;
   }

   public float GetWhiteNoise(float x, float y, float z, float w) {
      int var5 = this.FloatCast2Int(var1);
      int var6 = this.FloatCast2Int(var2);
      int var7 = this.FloatCast2Int(var3);
      int var8 = this.FloatCast2Int(var4);
      return ValCoord4D(this.m_seed, var5, var6, var7, var8);
   }

   public float GetWhiteNoise(double x, double y, double z) {
      int var7 = this.FloatCast2Int((float)var1);
      int var8 = this.FloatCast2Int((float)var3);
      int var9 = this.FloatCast2Int((float)var5);
      return ValCoord3D(this.m_seed, var7, var8, var9);
   }

   public float GetWhiteNoise(double x, double y) {
      int var5 = this.FloatCast2Int((float)var1);
      int var6 = this.FloatCast2Int((float)var3);
      return ValCoord2D(this.m_seed, var5, var6);
   }

   public float GetWhiteNoiseInt(int x, int y, int z, int w) {
      return ValCoord4D(this.m_seed, var1, var2, var3, var4);
   }

   public float GetWhiteNoiseInt(int x, int y, int z) {
      return ValCoord3D(this.m_seed, var1, var2, var3);
   }

   public float GetWhiteNoiseInt(int x, int y) {
      return ValCoord2D(this.m_seed, var1, var2);
   }

   public float GetValueFractal(float x, float y, float z) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      var3 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SingleValueFractalFBM(var1, var2, var3);
      case 1:
         return this.SingleValueFractalBillow(var1, var2, var3);
      case 2:
         return this.SingleValueFractalRigidMulti(var1, var2, var3);
      default:
         return 0.0F;
      }
   }

   private float SingleValueFractalFBM(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = this.SingleValue(var4, var1, var2, var3);
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += this.SingleValue(var4, var1, var2, var3) * var6;
      }

      return var5 * this.m_fractalBounding;
   }

   private float SingleValueFractalBillow(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = Math.abs(this.SingleValue(var4, var1, var2, var3)) * 2.0F - 1.0F;
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += (Math.abs(this.SingleValue(var4, var1, var2, var3)) * 2.0F - 1.0F) * var6;
      }

      return var5 * this.m_fractalBounding;
   }

   private float SingleValueFractalRigidMulti(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = 1.0F - Math.abs(this.SingleValue(var4, var1, var2, var3));
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 -= (1.0F - Math.abs(this.SingleValue(var4, var1, var2, var3))) * var6;
      }

      return var5;
   }

   public float GetValue(float x, float y, float z) {
      return this.SingleValue(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency, var3 * this.m_frequency);
   }

   private float SingleValue(int seed, float x, float y, float z) {
      int var5 = FastFloor(var2);
      int var6 = FastFloor(var3);
      int var7 = FastFloor(var4);
      int var8 = var5 + 1;
      int var9 = var6 + 1;
      int var10 = var7 + 1;
      float var11;
      float var12;
      float var13;
      switch(this.m_interp.ordinal()) {
      case 0:
      default:
         var11 = var2 - (float)var5;
         var12 = var3 - (float)var6;
         var13 = var4 - (float)var7;
         break;
      case 1:
         var11 = InterpHermiteFunc(var2 - (float)var5);
         var12 = InterpHermiteFunc(var3 - (float)var6);
         var13 = InterpHermiteFunc(var4 - (float)var7);
         break;
      case 2:
         var11 = InterpQuinticFunc(var2 - (float)var5);
         var12 = InterpQuinticFunc(var3 - (float)var6);
         var13 = InterpQuinticFunc(var4 - (float)var7);
      }

      float var14 = Lerp(ValCoord3D(var1, var5, var6, var7), ValCoord3D(var1, var8, var6, var7), var11);
      float var15 = Lerp(ValCoord3D(var1, var5, var9, var7), ValCoord3D(var1, var8, var9, var7), var11);
      float var16 = Lerp(ValCoord3D(var1, var5, var6, var10), ValCoord3D(var1, var8, var6, var10), var11);
      float var17 = Lerp(ValCoord3D(var1, var5, var9, var10), ValCoord3D(var1, var8, var9, var10), var11);
      float var18 = Lerp(var14, var15, var12);
      float var19 = Lerp(var16, var17, var12);
      return Lerp(var18, var19, var13);
   }

   public float GetValueFractal(float x, float y) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SingleValueFractalFBM(var1, var2);
      case 1:
         return this.SingleValueFractalBillow(var1, var2);
      case 2:
         return this.SingleValueFractalRigidMulti(var1, var2);
      default:
         return 0.0F;
      }
   }

   private float SingleValueFractalFBM(float x, float y) {
      int var3 = this.m_seed;
      float var4 = this.SingleValue(var3, var1, var2);
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += this.SingleValue(var3, var1, var2) * var5;
      }

      return var4 * this.m_fractalBounding;
   }

   private float SingleValueFractalBillow(float x, float y) {
      int var3 = this.m_seed;
      float var4 = Math.abs(this.SingleValue(var3, var1, var2)) * 2.0F - 1.0F;
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += (Math.abs(this.SingleValue(var3, var1, var2)) * 2.0F - 1.0F) * var5;
      }

      return var4 * this.m_fractalBounding;
   }

   private float SingleValueFractalRigidMulti(float x, float y) {
      int var3 = this.m_seed;
      float var4 = 1.0F - Math.abs(this.SingleValue(var3, var1, var2));
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 -= (1.0F - Math.abs(this.SingleValue(var3, var1, var2))) * var5;
      }

      return var4;
   }

   public float GetValue(float x, float y) {
      return this.SingleValue(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency);
   }

   private float SingleValue(int seed, float x, float y) {
      int var4 = FastFloor(var2);
      int var5 = FastFloor(var3);
      int var6 = var4 + 1;
      int var7 = var5 + 1;
      float var8;
      float var9;
      switch(this.m_interp.ordinal()) {
      case 0:
      default:
         var8 = var2 - (float)var4;
         var9 = var3 - (float)var5;
         break;
      case 1:
         var8 = InterpHermiteFunc(var2 - (float)var4);
         var9 = InterpHermiteFunc(var3 - (float)var5);
         break;
      case 2:
         var8 = InterpQuinticFunc(var2 - (float)var4);
         var9 = InterpQuinticFunc(var3 - (float)var5);
      }

      float var10 = Lerp(ValCoord2D(var1, var4, var5), ValCoord2D(var1, var6, var5), var8);
      float var11 = Lerp(ValCoord2D(var1, var4, var7), ValCoord2D(var1, var6, var7), var8);
      return Lerp(var10, var11, var9);
   }

   public float GetPerlinFractal(float x, float y, float z) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      var3 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SinglePerlinFractalFBM(var1, var2, var3);
      case 1:
         return this.SinglePerlinFractalBillow(var1, var2, var3);
      case 2:
         return this.SinglePerlinFractalRigidMulti(var1, var2, var3);
      default:
         return 0.0F;
      }
   }

   private float SinglePerlinFractalFBM(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = this.SinglePerlin(var4, var1, var2, var3);
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += this.SinglePerlin(var4, var1, var2, var3) * var6;
      }

      return var5 * this.m_fractalBounding;
   }

   private float SinglePerlinFractalBillow(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = Math.abs(this.SinglePerlin(var4, var1, var2, var3)) * 2.0F - 1.0F;
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += (Math.abs(this.SinglePerlin(var4, var1, var2, var3)) * 2.0F - 1.0F) * var6;
      }

      return var5 * this.m_fractalBounding;
   }

   private float SinglePerlinFractalRigidMulti(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = 1.0F - Math.abs(this.SinglePerlin(var4, var1, var2, var3));
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 -= (1.0F - Math.abs(this.SinglePerlin(var4, var1, var2, var3))) * var6;
      }

      return var5;
   }

   public float GetPerlin(float x, float y, float z) {
      return this.SinglePerlin(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency, var3 * this.m_frequency);
   }

   private float SinglePerlin(int seed, float x, float y, float z) {
      int var5 = FastFloor(var2);
      int var6 = FastFloor(var3);
      int var7 = FastFloor(var4);
      int var8 = var5 + 1;
      int var9 = var6 + 1;
      int var10 = var7 + 1;
      float var11;
      float var12;
      float var13;
      switch(this.m_interp.ordinal()) {
      case 0:
      default:
         var11 = var2 - (float)var5;
         var12 = var3 - (float)var6;
         var13 = var4 - (float)var7;
         break;
      case 1:
         var11 = InterpHermiteFunc(var2 - (float)var5);
         var12 = InterpHermiteFunc(var3 - (float)var6);
         var13 = InterpHermiteFunc(var4 - (float)var7);
         break;
      case 2:
         var11 = InterpQuinticFunc(var2 - (float)var5);
         var12 = InterpQuinticFunc(var3 - (float)var6);
         var13 = InterpQuinticFunc(var4 - (float)var7);
      }

      float var14 = var2 - (float)var5;
      float var15 = var3 - (float)var6;
      float var16 = var4 - (float)var7;
      float var17 = var14 - 1.0F;
      float var18 = var15 - 1.0F;
      float var19 = var16 - 1.0F;
      float var20 = Lerp(GradCoord3D(var1, var5, var6, var7, var14, var15, var16), GradCoord3D(var1, var8, var6, var7, var17, var15, var16), var11);
      float var21 = Lerp(GradCoord3D(var1, var5, var9, var7, var14, var18, var16), GradCoord3D(var1, var8, var9, var7, var17, var18, var16), var11);
      float var22 = Lerp(GradCoord3D(var1, var5, var6, var10, var14, var15, var19), GradCoord3D(var1, var8, var6, var10, var17, var15, var19), var11);
      float var23 = Lerp(GradCoord3D(var1, var5, var9, var10, var14, var18, var19), GradCoord3D(var1, var8, var9, var10, var17, var18, var19), var11);
      float var24 = Lerp(var20, var21, var12);
      float var25 = Lerp(var22, var23, var12);
      return Lerp(var24, var25, var13);
   }

   public float GetPerlinFractal(float x, float y) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SinglePerlinFractalFBM(var1, var2);
      case 1:
         return this.SinglePerlinFractalBillow(var1, var2);
      case 2:
         return this.SinglePerlinFractalRigidMulti(var1, var2);
      default:
         return 0.0F;
      }
   }

   private float SinglePerlinFractalFBM(float x, float y) {
      int var3 = this.m_seed;
      float var4 = this.SinglePerlin(var3, var1, var2);
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += this.SinglePerlin(var3, var1, var2) * var5;
      }

      return var4 * this.m_fractalBounding;
   }

   private float SinglePerlinFractalBillow(float x, float y) {
      int var3 = this.m_seed;
      float var4 = Math.abs(this.SinglePerlin(var3, var1, var2)) * 2.0F - 1.0F;
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += (Math.abs(this.SinglePerlin(var3, var1, var2)) * 2.0F - 1.0F) * var5;
      }

      return var4 * this.m_fractalBounding;
   }

   private float SinglePerlinFractalRigidMulti(float x, float y) {
      int var3 = this.m_seed;
      float var4 = 1.0F - Math.abs(this.SinglePerlin(var3, var1, var2));
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 -= (1.0F - Math.abs(this.SinglePerlin(var3, var1, var2))) * var5;
      }

      return var4;
   }

   public float GetPerlin(float x, float y) {
      return this.SinglePerlin(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency);
   }

   private float SinglePerlin(int seed, float x, float y) {
      int var4 = FastFloor(var2);
      int var5 = FastFloor(var3);
      int var6 = var4 + 1;
      int var7 = var5 + 1;
      float var8;
      float var9;
      switch(this.m_interp.ordinal()) {
      case 0:
      default:
         var8 = var2 - (float)var4;
         var9 = var3 - (float)var5;
         break;
      case 1:
         var8 = InterpHermiteFunc(var2 - (float)var4);
         var9 = InterpHermiteFunc(var3 - (float)var5);
         break;
      case 2:
         var8 = InterpQuinticFunc(var2 - (float)var4);
         var9 = InterpQuinticFunc(var3 - (float)var5);
      }

      float var10 = var2 - (float)var4;
      float var11 = var3 - (float)var5;
      float var12 = var10 - 1.0F;
      float var13 = var11 - 1.0F;
      float var14 = Lerp(GradCoord2D(var1, var4, var5, var10, var11), GradCoord2D(var1, var6, var5, var12, var11), var8);
      float var15 = Lerp(GradCoord2D(var1, var4, var7, var10, var13), GradCoord2D(var1, var6, var7, var12, var13), var8);
      return Lerp(var14, var15, var9);
   }

   public float GetSimplexFractal(float x, float y, float z) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      var3 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SingleSimplexFractalFBM(var1, var2, var3);
      case 1:
         return this.SingleSimplexFractalBillow(var1, var2, var3);
      case 2:
         return this.SingleSimplexFractalRigidMulti(var1, var2, var3);
      default:
         return 0.0F;
      }
   }

   private float SingleSimplexFractalFBM(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = this.SingleSimplex(var4, var1, var2, var3);
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += this.SingleSimplex(var4, var1, var2, var3) * var6;
      }

      return var5 * this.m_fractalBounding;
   }

   private float SingleSimplexFractalBillow(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = Math.abs(this.SingleSimplex(var4, var1, var2, var3)) * 2.0F - 1.0F;
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += (Math.abs(this.SingleSimplex(var4, var1, var2, var3)) * 2.0F - 1.0F) * var6;
      }

      return var5 * this.m_fractalBounding;
   }

   private float SingleSimplexFractalRigidMulti(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = 1.0F - Math.abs(this.SingleSimplex(var4, var1, var2, var3));
      float var6 = 1.0F;

      for(int var7 = 1; var7 < this.m_octaves; ++var7) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 -= (1.0F - Math.abs(this.SingleSimplex(var4, var1, var2, var3))) * var6;
      }

      return var5;
   }

   public float GetSimplex(float x, float y, float z) {
      return this.SingleSimplex(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency, var3 * this.m_frequency);
   }

   private float SingleSimplex(int seed, float x, float y, float z) {
      float var5 = (var2 + var3 + var4) * 0.33333334F;
      int var6 = FastFloor(var2 + var5);
      int var7 = FastFloor(var3 + var5);
      int var8 = FastFloor(var4 + var5);
      var5 = (float)(var6 + var7 + var8) * 0.16666667F;
      float var9 = var2 - ((float)var6 - var5);
      float var10 = var3 - ((float)var7 - var5);
      float var11 = var4 - ((float)var8 - var5);
      byte var12;
      byte var13;
      byte var14;
      byte var15;
      byte var16;
      byte var17;
      if (var9 >= var10) {
         if (var10 >= var11) {
            var12 = 1;
            var13 = 0;
            var14 = 0;
            var15 = 1;
            var16 = 1;
            var17 = 0;
         } else if (var9 >= var11) {
            var12 = 1;
            var13 = 0;
            var14 = 0;
            var15 = 1;
            var16 = 0;
            var17 = 1;
         } else {
            var12 = 0;
            var13 = 0;
            var14 = 1;
            var15 = 1;
            var16 = 0;
            var17 = 1;
         }
      } else if (var10 < var11) {
         var12 = 0;
         var13 = 0;
         var14 = 1;
         var15 = 0;
         var16 = 1;
         var17 = 1;
      } else if (var9 < var11) {
         var12 = 0;
         var13 = 1;
         var14 = 0;
         var15 = 0;
         var16 = 1;
         var17 = 1;
      } else {
         var12 = 0;
         var13 = 1;
         var14 = 0;
         var15 = 1;
         var16 = 1;
         var17 = 0;
      }

      float var18 = var9 - (float)var12 + 0.16666667F;
      float var19 = var10 - (float)var13 + 0.16666667F;
      float var20 = var11 - (float)var14 + 0.16666667F;
      float var21 = var9 - (float)var15 + 0.33333334F;
      float var22 = var10 - (float)var16 + 0.33333334F;
      float var23 = var11 - (float)var17 + 0.33333334F;
      float var24 = var9 + -0.5F;
      float var25 = var10 + -0.5F;
      float var26 = var11 + -0.5F;
      var5 = 0.6F - var9 * var9 - var10 * var10 - var11 * var11;
      float var27;
      if (var5 < 0.0F) {
         var27 = 0.0F;
      } else {
         var5 *= var5;
         var27 = var5 * var5 * GradCoord3D(var1, var6, var7, var8, var9, var10, var11);
      }

      var5 = 0.6F - var18 * var18 - var19 * var19 - var20 * var20;
      float var28;
      if (var5 < 0.0F) {
         var28 = 0.0F;
      } else {
         var5 *= var5;
         var28 = var5 * var5 * GradCoord3D(var1, var6 + var12, var7 + var13, var8 + var14, var18, var19, var20);
      }

      var5 = 0.6F - var21 * var21 - var22 * var22 - var23 * var23;
      float var29;
      if (var5 < 0.0F) {
         var29 = 0.0F;
      } else {
         var5 *= var5;
         var29 = var5 * var5 * GradCoord3D(var1, var6 + var15, var7 + var16, var8 + var17, var21, var22, var23);
      }

      var5 = 0.6F - var24 * var24 - var25 * var25 - var26 * var26;
      float var30;
      if (var5 < 0.0F) {
         var30 = 0.0F;
      } else {
         var5 *= var5;
         var30 = var5 * var5 * GradCoord3D(var1, var6 + 1, var7 + 1, var8 + 1, var24, var25, var26);
      }

      return 32.0F * (var27 + var28 + var29 + var30);
   }

   public float GetSimplexFractal(float x, float y) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SingleSimplexFractalFBM(var1, var2);
      case 1:
         return this.SingleSimplexFractalBillow(var1, var2);
      case 2:
         return this.SingleSimplexFractalRigidMulti(var1, var2);
      default:
         return 0.0F;
      }
   }

   private float SingleSimplexFractalFBM(float x, float y) {
      int var3 = this.m_seed;
      float var4 = this.SingleSimplex(var3, var1, var2);
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += this.SingleSimplex(var3, var1, var2) * var5;
      }

      return var4 * this.m_fractalBounding;
   }

   private float SingleSimplexFractalBillow(float x, float y) {
      int var3 = this.m_seed;
      float var4 = Math.abs(this.SingleSimplex(var3, var1, var2)) * 2.0F - 1.0F;
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += (Math.abs(this.SingleSimplex(var3, var1, var2)) * 2.0F - 1.0F) * var5;
      }

      return var4 * this.m_fractalBounding;
   }

   private float SingleSimplexFractalRigidMulti(float x, float y) {
      int var3 = this.m_seed;
      float var4 = 1.0F - Math.abs(this.SingleSimplex(var3, var1, var2));
      float var5 = 1.0F;

      for(int var6 = 1; var6 < this.m_octaves; ++var6) {
         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 -= (1.0F - Math.abs(this.SingleSimplex(var3, var1, var2))) * var5;
      }

      return var4;
   }

   public float GetSimplex(float x, float y) {
      return this.SingleSimplex(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency);
   }

   private float SingleSimplex(int seed, float x, float y) {
      float var4 = (var2 + var3) * 0.3660254F;
      int var5 = FastFloor(var2 + var4);
      int var6 = FastFloor(var3 + var4);
      var4 = (float)(var5 + var6) * 0.21132487F;
      float var7 = (float)var5 - var4;
      float var8 = (float)var6 - var4;
      float var9 = var2 - var7;
      float var10 = var3 - var8;
      byte var11;
      byte var12;
      if (var9 > var10) {
         var11 = 1;
         var12 = 0;
      } else {
         var11 = 0;
         var12 = 1;
      }

      float var13 = var9 - (float)var11 + 0.21132487F;
      float var14 = var10 - (float)var12 + 0.21132487F;
      float var15 = var9 - 1.0F + 0.42264974F;
      float var16 = var10 - 1.0F + 0.42264974F;
      var4 = 0.5F - var9 * var9 - var10 * var10;
      float var17;
      if (var4 < 0.0F) {
         var17 = 0.0F;
      } else {
         var4 *= var4;
         var17 = var4 * var4 * GradCoord2D(var1, var5, var6, var9, var10);
      }

      var4 = 0.5F - var13 * var13 - var14 * var14;
      float var18;
      if (var4 < 0.0F) {
         var18 = 0.0F;
      } else {
         var4 *= var4;
         var18 = var4 * var4 * GradCoord2D(var1, var5 + var11, var6 + var12, var13, var14);
      }

      var4 = 0.5F - var15 * var15 - var16 * var16;
      float var19;
      if (var4 < 0.0F) {
         var19 = 0.0F;
      } else {
         var4 *= var4;
         var19 = var4 * var4 * GradCoord2D(var1, var5 + 1, var6 + 1, var15, var16);
      }

      return 50.0F * (var17 + var18 + var19);
   }

   public float GetSimplex(float x, float y, float z, float w) {
      return this.SingleSimplex(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency, var3 * this.m_frequency, var4 * this.m_frequency);
   }

   private float SingleSimplex(int seed, float x, float y, float z, float w) {
      float var11 = (var2 + var3 + var4 + var5) * 0.309017F;
      int var12 = FastFloor(var2 + var11);
      int var13 = FastFloor(var3 + var11);
      int var14 = FastFloor(var4 + var11);
      int var15 = FastFloor(var5 + var11);
      var11 = (float)(var12 + var13 + var14 + var15) * 0.1381966F;
      float var16 = (float)var12 - var11;
      float var17 = (float)var13 - var11;
      float var18 = (float)var14 - var11;
      float var19 = (float)var15 - var11;
      float var20 = var2 - var16;
      float var21 = var3 - var17;
      float var22 = var4 - var18;
      float var23 = var5 - var19;
      int var24 = var20 > var21 ? 32 : 0;
      var24 += var20 > var22 ? 16 : 0;
      var24 += var21 > var22 ? 8 : 0;
      var24 += var20 > var23 ? 4 : 0;
      var24 += var21 > var23 ? 2 : 0;
      var24 += var22 > var23 ? 1 : 0;
      var24 <<= 2;
      int var25 = SIMPLEX_4D[var24] >= 3 ? 1 : 0;
      int var26 = SIMPLEX_4D[var24] >= 2 ? 1 : 0;
      int var27 = SIMPLEX_4D[var24++] >= 1 ? 1 : 0;
      int var28 = SIMPLEX_4D[var24] >= 3 ? 1 : 0;
      int var29 = SIMPLEX_4D[var24] >= 2 ? 1 : 0;
      int var30 = SIMPLEX_4D[var24++] >= 1 ? 1 : 0;
      int var31 = SIMPLEX_4D[var24] >= 3 ? 1 : 0;
      int var32 = SIMPLEX_4D[var24] >= 2 ? 1 : 0;
      int var33 = SIMPLEX_4D[var24++] >= 1 ? 1 : 0;
      int var34 = SIMPLEX_4D[var24] >= 3 ? 1 : 0;
      int var35 = SIMPLEX_4D[var24] >= 2 ? 1 : 0;
      int var36 = SIMPLEX_4D[var24] >= 1 ? 1 : 0;
      float var37 = var20 - (float)var25 + 0.1381966F;
      float var38 = var21 - (float)var28 + 0.1381966F;
      float var39 = var22 - (float)var31 + 0.1381966F;
      float var40 = var23 - (float)var34 + 0.1381966F;
      float var41 = var20 - (float)var26 + 0.2763932F;
      float var42 = var21 - (float)var29 + 0.2763932F;
      float var43 = var22 - (float)var32 + 0.2763932F;
      float var44 = var23 - (float)var35 + 0.2763932F;
      float var45 = var20 - (float)var27 + 0.41458982F;
      float var46 = var21 - (float)var30 + 0.41458982F;
      float var47 = var22 - (float)var33 + 0.41458982F;
      float var48 = var23 - (float)var36 + 0.41458982F;
      float var49 = var20 - 1.0F + 0.5527864F;
      float var50 = var21 - 1.0F + 0.5527864F;
      float var51 = var22 - 1.0F + 0.5527864F;
      float var52 = var23 - 1.0F + 0.5527864F;
      var11 = 0.6F - var20 * var20 - var21 * var21 - var22 * var22 - var23 * var23;
      float var6;
      if (var11 < 0.0F) {
         var6 = 0.0F;
      } else {
         var11 *= var11;
         var6 = var11 * var11 * GradCoord4D(var1, var12, var13, var14, var15, var20, var21, var22, var23);
      }

      var11 = 0.6F - var37 * var37 - var38 * var38 - var39 * var39 - var40 * var40;
      float var7;
      if (var11 < 0.0F) {
         var7 = 0.0F;
      } else {
         var11 *= var11;
         var7 = var11 * var11 * GradCoord4D(var1, var12 + var25, var13 + var28, var14 + var31, var15 + var34, var37, var38, var39, var40);
      }

      var11 = 0.6F - var41 * var41 - var42 * var42 - var43 * var43 - var44 * var44;
      float var8;
      if (var11 < 0.0F) {
         var8 = 0.0F;
      } else {
         var11 *= var11;
         var8 = var11 * var11 * GradCoord4D(var1, var12 + var26, var13 + var29, var14 + var32, var15 + var35, var41, var42, var43, var44);
      }

      var11 = 0.6F - var45 * var45 - var46 * var46 - var47 * var47 - var48 * var48;
      float var9;
      if (var11 < 0.0F) {
         var9 = 0.0F;
      } else {
         var11 *= var11;
         var9 = var11 * var11 * GradCoord4D(var1, var12 + var27, var13 + var30, var14 + var33, var15 + var36, var45, var46, var47, var48);
      }

      var11 = 0.6F - var49 * var49 - var50 * var50 - var51 * var51 - var52 * var52;
      float var10;
      if (var11 < 0.0F) {
         var10 = 0.0F;
      } else {
         var11 *= var11;
         var10 = var11 * var11 * GradCoord4D(var1, var12 + 1, var13 + 1, var14 + 1, var15 + 1, var49, var50, var51, var52);
      }

      return 27.0F * (var6 + var7 + var8 + var9 + var10);
   }

   public float GetCubicFractal(float x, float y, float z) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      var3 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SingleCubicFractalFBM(var1, var2, var3);
      case 1:
         return this.SingleCubicFractalBillow(var1, var2, var3);
      case 2:
         return this.SingleCubicFractalRigidMulti(var1, var2, var3);
      default:
         return 0.0F;
      }
   }

   private float SingleCubicFractalFBM(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = this.SingleCubic(var4, var1, var2, var3);
      float var6 = 1.0F;
      int var7 = 0;

      while(true) {
         ++var7;
         if (var7 >= this.m_octaves) {
            return var5 * this.m_fractalBounding;
         }

         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += this.SingleCubic(var4, var1, var2, var3) * var6;
      }
   }

   private float SingleCubicFractalBillow(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = Math.abs(this.SingleCubic(var4, var1, var2, var3)) * 2.0F - 1.0F;
      float var6 = 1.0F;
      int var7 = 0;

      while(true) {
         ++var7;
         if (var7 >= this.m_octaves) {
            return var5 * this.m_fractalBounding;
         }

         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 += (Math.abs(this.SingleCubic(var4, var1, var2, var3)) * 2.0F - 1.0F) * var6;
      }
   }

   private float SingleCubicFractalRigidMulti(float x, float y, float z) {
      int var4 = this.m_seed;
      float var5 = 1.0F - Math.abs(this.SingleCubic(var4, var1, var2, var3));
      float var6 = 1.0F;
      int var7 = 0;

      while(true) {
         ++var7;
         if (var7 >= this.m_octaves) {
            return var5;
         }

         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var3 *= this.m_lacunarity;
         var6 *= this.m_gain;
         ++var4;
         var5 -= (1.0F - Math.abs(this.SingleCubic(var4, var1, var2, var3))) * var6;
      }
   }

   public float GetCubic(float x, float y, float z) {
      return this.SingleCubic(this.m_seed, var1 * this.m_frequency, var2 * this.m_frequency, var3 * this.m_frequency);
   }

   private float SingleCubic(int seed, float x, float y, float z) {
      int var5 = FastFloor(var2);
      int var6 = FastFloor(var3);
      int var7 = FastFloor(var4);
      int var8 = var5 - 1;
      int var9 = var6 - 1;
      int var10 = var7 - 1;
      int var11 = var5 + 1;
      int var12 = var6 + 1;
      int var13 = var7 + 1;
      int var14 = var5 + 2;
      int var15 = var6 + 2;
      int var16 = var7 + 2;
      float var17 = var2 - (float)var5;
      float var18 = var3 - (float)var6;
      float var19 = var4 - (float)var7;
      return CubicLerp(CubicLerp(CubicLerp(ValCoord3D(var1, var8, var9, var10), ValCoord3D(var1, var5, var9, var10), ValCoord3D(var1, var11, var9, var10), ValCoord3D(var1, var14, var9, var10), var17), CubicLerp(ValCoord3D(var1, var8, var6, var10), ValCoord3D(var1, var5, var6, var10), ValCoord3D(var1, var11, var6, var10), ValCoord3D(var1, var14, var6, var10), var17), CubicLerp(ValCoord3D(var1, var8, var12, var10), ValCoord3D(var1, var5, var12, var10), ValCoord3D(var1, var11, var12, var10), ValCoord3D(var1, var14, var12, var10), var17), CubicLerp(ValCoord3D(var1, var8, var15, var10), ValCoord3D(var1, var5, var15, var10), ValCoord3D(var1, var11, var15, var10), ValCoord3D(var1, var14, var15, var10), var17), var18), CubicLerp(CubicLerp(ValCoord3D(var1, var8, var9, var7), ValCoord3D(var1, var5, var9, var7), ValCoord3D(var1, var11, var9, var7), ValCoord3D(var1, var14, var9, var7), var17), CubicLerp(ValCoord3D(var1, var8, var6, var7), ValCoord3D(var1, var5, var6, var7), ValCoord3D(var1, var11, var6, var7), ValCoord3D(var1, var14, var6, var7), var17), CubicLerp(ValCoord3D(var1, var8, var12, var7), ValCoord3D(var1, var5, var12, var7), ValCoord3D(var1, var11, var12, var7), ValCoord3D(var1, var14, var12, var7), var17), CubicLerp(ValCoord3D(var1, var8, var15, var7), ValCoord3D(var1, var5, var15, var7), ValCoord3D(var1, var11, var15, var7), ValCoord3D(var1, var14, var15, var7), var17), var18), CubicLerp(CubicLerp(ValCoord3D(var1, var8, var9, var13), ValCoord3D(var1, var5, var9, var13), ValCoord3D(var1, var11, var9, var13), ValCoord3D(var1, var14, var9, var13), var17), CubicLerp(ValCoord3D(var1, var8, var6, var13), ValCoord3D(var1, var5, var6, var13), ValCoord3D(var1, var11, var6, var13), ValCoord3D(var1, var14, var6, var13), var17), CubicLerp(ValCoord3D(var1, var8, var12, var13), ValCoord3D(var1, var5, var12, var13), ValCoord3D(var1, var11, var12, var13), ValCoord3D(var1, var14, var12, var13), var17), CubicLerp(ValCoord3D(var1, var8, var15, var13), ValCoord3D(var1, var5, var15, var13), ValCoord3D(var1, var11, var15, var13), ValCoord3D(var1, var14, var15, var13), var17), var18), CubicLerp(CubicLerp(ValCoord3D(var1, var8, var9, var16), ValCoord3D(var1, var5, var9, var16), ValCoord3D(var1, var11, var9, var16), ValCoord3D(var1, var14, var9, var16), var17), CubicLerp(ValCoord3D(var1, var8, var6, var16), ValCoord3D(var1, var5, var6, var16), ValCoord3D(var1, var11, var6, var16), ValCoord3D(var1, var14, var6, var16), var17), CubicLerp(ValCoord3D(var1, var8, var12, var16), ValCoord3D(var1, var5, var12, var16), ValCoord3D(var1, var11, var12, var16), ValCoord3D(var1, var14, var12, var16), var17), CubicLerp(ValCoord3D(var1, var8, var15, var16), ValCoord3D(var1, var5, var15, var16), ValCoord3D(var1, var11, var15, var16), ValCoord3D(var1, var14, var15, var16), var17), var18), var19) * 0.2962963F;
   }

   public float GetCubicFractal(float x, float y) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      switch(this.m_fractalType.ordinal()) {
      case 0:
         return this.SingleCubicFractalFBM(var1, var2);
      case 1:
         return this.SingleCubicFractalBillow(var1, var2);
      case 2:
         return this.SingleCubicFractalRigidMulti(var1, var2);
      default:
         return 0.0F;
      }
   }

   private float SingleCubicFractalFBM(float x, float y) {
      int var3 = this.m_seed;
      float var4 = this.SingleCubic(var3, var1, var2);
      float var5 = 1.0F;
      int var6 = 0;

      while(true) {
         ++var6;
         if (var6 >= this.m_octaves) {
            return var4 * this.m_fractalBounding;
         }

         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += this.SingleCubic(var3, var1, var2) * var5;
      }
   }

   private float SingleCubicFractalBillow(float x, float y) {
      int var3 = this.m_seed;
      float var4 = Math.abs(this.SingleCubic(var3, var1, var2)) * 2.0F - 1.0F;
      float var5 = 1.0F;
      int var6 = 0;

      while(true) {
         ++var6;
         if (var6 >= this.m_octaves) {
            return var4 * this.m_fractalBounding;
         }

         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 += (Math.abs(this.SingleCubic(var3, var1, var2)) * 2.0F - 1.0F) * var5;
      }
   }

   private float SingleCubicFractalRigidMulti(float x, float y) {
      int var3 = this.m_seed;
      float var4 = 1.0F - Math.abs(this.SingleCubic(var3, var1, var2));
      float var5 = 1.0F;
      int var6 = 0;

      while(true) {
         ++var6;
         if (var6 >= this.m_octaves) {
            return var4;
         }

         var1 *= this.m_lacunarity;
         var2 *= this.m_lacunarity;
         var5 *= this.m_gain;
         ++var3;
         var4 -= (1.0F - Math.abs(this.SingleCubic(var3, var1, var2))) * var5;
      }
   }

   public float GetCubic(float x, float y) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      return this.SingleCubic(0, var1, var2);
   }

   private float SingleCubic(int seed, float x, float y) {
      int var4 = FastFloor(var2);
      int var5 = FastFloor(var3);
      int var6 = var4 - 1;
      int var7 = var5 - 1;
      int var8 = var4 + 1;
      int var9 = var5 + 1;
      int var10 = var4 + 2;
      int var11 = var5 + 2;
      float var12 = var2 - (float)var4;
      float var13 = var3 - (float)var5;
      return CubicLerp(CubicLerp(ValCoord2D(var1, var6, var7), ValCoord2D(var1, var4, var7), ValCoord2D(var1, var8, var7), ValCoord2D(var1, var10, var7), var12), CubicLerp(ValCoord2D(var1, var6, var5), ValCoord2D(var1, var4, var5), ValCoord2D(var1, var8, var5), ValCoord2D(var1, var10, var5), var12), CubicLerp(ValCoord2D(var1, var6, var9), ValCoord2D(var1, var4, var9), ValCoord2D(var1, var8, var9), ValCoord2D(var1, var10, var9), var12), CubicLerp(ValCoord2D(var1, var6, var11), ValCoord2D(var1, var4, var11), ValCoord2D(var1, var8, var11), ValCoord2D(var1, var10, var11), var12), var13) * 0.44444445F;
   }

   public float GetCellular(float x, float y, float z) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      var3 *= this.m_frequency;
      switch(this.m_cellularReturnType.ordinal()) {
      case 0:
      case 1:
      case 2:
         return this.SingleCellular(var1, var2, var3);
      default:
         return this.SingleCellular2Edge(var1, var2, var3);
      }
   }

   private float SingleCellular(float x, float y, float z) {
      float var7;
      int var8;
      int var9;
      int var10;
      int var4 = FastRound(var1);
      int var5 = FastRound(var2);
      int var6 = FastRound(var3);
      var7 = 999999.0F;
      var8 = 0;
      var9 = 0;
      var10 = 0;
      int var11;
      int var12;
      int var13;
      FastNoise.Float3 var14;
      float var15;
      float var16;
      float var17;
      float var18;
      label103:
      switch(this.m_cellularDistanceFunction.ordinal()) {
      case 0:
         var11 = var4 - 1;

         while(true) {
            if (var11 > var4 + 1) {
               break label103;
            }

            for(var12 = var5 - 1; var12 <= var5 + 1; ++var12) {
               for(var13 = var6 - 1; var13 <= var6 + 1; ++var13) {
                  var14 = CELL_3D[Hash3D(this.m_seed, var11, var12, var13) & 255];
                  var15 = (float)var11 - var1 + var14.x;
                  var16 = (float)var12 - var2 + var14.y;
                  var17 = (float)var13 - var3 + var14.z;
                  var18 = var15 * var15 + var16 * var16 + var17 * var17;
                  if (var18 < var7) {
                     var7 = var18;
                     var8 = var11;
                     var9 = var12;
                     var10 = var13;
                  }
               }
            }

            ++var11;
         }
      case 1:
         var11 = var4 - 1;

         while(true) {
            if (var11 > var4 + 1) {
               break label103;
            }

            for(var12 = var5 - 1; var12 <= var5 + 1; ++var12) {
               for(var13 = var6 - 1; var13 <= var6 + 1; ++var13) {
                  var14 = CELL_3D[Hash3D(this.m_seed, var11, var12, var13) & 255];
                  var15 = (float)var11 - var1 + var14.x;
                  var16 = (float)var12 - var2 + var14.y;
                  var17 = (float)var13 - var3 + var14.z;
                  var18 = Math.abs(var15) + Math.abs(var16) + Math.abs(var17);
                  if (var18 < var7) {
                     var7 = var18;
                     var8 = var11;
                     var9 = var12;
                     var10 = var13;
                  }
               }
            }

            ++var11;
         }
      case 2:
         for(var11 = var4 - 1; var11 <= var4 + 1; ++var11) {
            for(var12 = var5 - 1; var12 <= var5 + 1; ++var12) {
               for(var13 = var6 - 1; var13 <= var6 + 1; ++var13) {
                  var14 = CELL_3D[Hash3D(this.m_seed, var11, var12, var13) & 255];
                  var15 = (float)var11 - var1 + var14.x;
                  var16 = (float)var12 - var2 + var14.y;
                  var17 = (float)var13 - var3 + var14.z;
                  var18 = Math.abs(var15) + Math.abs(var16) + Math.abs(var17) + var15 * var15 + var16 * var16 + var17 * var17;
                  if (var18 < var7) {
                     var7 = var18;
                     var8 = var11;
                     var9 = var12;
                     var10 = var13;
                  }
               }
            }
         }
      }

      switch(this.m_cellularReturnType.ordinal()) {
      case 0:
         return ValCoord3D(0, var8, var9, var10);
      case 1:
         FastNoise.Float3 var19 = CELL_3D[Hash3D(this.m_seed, var8, var9, var10) & 255];
         return this.m_cellularNoiseLookup.GetNoise((float)var8 + var19.x, (float)var9 + var19.y, (float)var10 + var19.z);
      case 2:
         return var7 - 1.0F;
      default:
         return 0.0F;
      }
   }

   private float SingleCellular2Edge(float x, float y, float z) {
      float var7;
      float var8;
      int var4 = FastRound(var1);
      int var5 = FastRound(var2);
      int var6 = FastRound(var3);
      var7 = 999999.0F;
      var8 = 999999.0F;
      int var9;
      int var10;
      int var11;
      FastNoise.Float3 var12;
      float var13;
      float var14;
      float var15;
      float var16;
      label94:
      switch(this.m_cellularDistanceFunction.ordinal()) {
      case 0:
         var9 = var4 - 1;

         while(true) {
            if (var9 > var4 + 1) {
               break label94;
            }

            for(var10 = var5 - 1; var10 <= var5 + 1; ++var10) {
               for(var11 = var6 - 1; var11 <= var6 + 1; ++var11) {
                  var12 = CELL_3D[Hash3D(this.m_seed, var9, var10, var11) & 255];
                  var13 = (float)var9 - var1 + var12.x;
                  var14 = (float)var10 - var2 + var12.y;
                  var15 = (float)var11 - var3 + var12.z;
                  var16 = var13 * var13 + var14 * var14 + var15 * var15;
                  var8 = Math.max(Math.min(var8, var16), var7);
                  var7 = Math.min(var7, var16);
               }
            }

            ++var9;
         }
      case 1:
         var9 = var4 - 1;

         while(true) {
            if (var9 > var4 + 1) {
               break label94;
            }

            for(var10 = var5 - 1; var10 <= var5 + 1; ++var10) {
               for(var11 = var6 - 1; var11 <= var6 + 1; ++var11) {
                  var12 = CELL_3D[Hash3D(this.m_seed, var9, var10, var11) & 255];
                  var13 = (float)var9 - var1 + var12.x;
                  var14 = (float)var10 - var2 + var12.y;
                  var15 = (float)var11 - var3 + var12.z;
                  var16 = Math.abs(var13) + Math.abs(var14) + Math.abs(var15);
                  var8 = Math.max(Math.min(var8, var16), var7);
                  var7 = Math.min(var7, var16);
               }
            }

            ++var9;
         }
      case 2:
         for(var9 = var4 - 1; var9 <= var4 + 1; ++var9) {
            for(var10 = var5 - 1; var10 <= var5 + 1; ++var10) {
               for(var11 = var6 - 1; var11 <= var6 + 1; ++var11) {
                  var12 = CELL_3D[Hash3D(this.m_seed, var9, var10, var11) & 255];
                  var13 = (float)var9 - var1 + var12.x;
                  var14 = (float)var10 - var2 + var12.y;
                  var15 = (float)var11 - var3 + var12.z;
                  var16 = Math.abs(var13) + Math.abs(var14) + Math.abs(var15) + var13 * var13 + var14 * var14 + var15 * var15;
                  var8 = Math.max(Math.min(var8, var16), var7);
                  var7 = Math.min(var7, var16);
               }
            }
         }
      }

      switch(this.m_cellularReturnType.ordinal()) {
      case 3:
         return var8 - 1.0F;
      case 4:
         return var8 + var7 - 1.0F;
      case 5:
         return var8 - var7 - 1.0F;
      case 6:
         return var8 * var7 - 1.0F;
      case 7:
         return var7 / var8 - 1.0F;
      default:
         return 0.0F;
      }
   }

   public float GetCellular(float x, float y, ProceduralStream<Double> sourceNoise, double iscale) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      switch(this.m_cellularReturnType.ordinal()) {
      case 0:
      case 1:
      case 2:
         return this.SingleCellular(var1, var2, var3, var4);
      default:
         return this.SingleCellular2Edge(var1, var2);
      }
   }

   public float GetCellular(float x, float y) {
      var1 *= this.m_frequency;
      var2 *= this.m_frequency;
      switch(this.m_cellularReturnType.ordinal()) {
      case 0:
      case 1:
      case 2:
         return this.SingleCellular(var1, var2);
      default:
         return this.SingleCellular2Edge(var1, var2);
      }
   }

   private float SingleCellular(float x, float y) {
      float var5;
      int var6;
      int var7;
      int var3 = FastRound(var1);
      int var4 = FastRound(var2);
      var5 = 999999.0F;
      var6 = 0;
      var7 = 0;
      int var8;
      int var9;
      FastNoise.Float2 var10;
      float var11;
      float var12;
      float var13;
      label76:
      switch(this.m_cellularDistanceFunction.ordinal()) {
      case 0:
      default:
         var8 = var3 - 1;

         while(true) {
            if (var8 > var3 + 1) {
               break label76;
            }

            for(var9 = var4 - 1; var9 <= var4 + 1; ++var9) {
               var10 = CELL_2D[Hash2D(this.m_seed, var8, var9) & 255];
               var11 = (float)var8 - var1 + var10.x;
               var12 = (float)var9 - var2 + var10.y;
               var13 = var11 * var11 + var12 * var12;
               if (var13 < var5) {
                  var5 = var13;
                  var6 = var8;
                  var7 = var9;
               }
            }

            ++var8;
         }
      case 1:
         var8 = var3 - 1;

         while(true) {
            if (var8 > var3 + 1) {
               break label76;
            }

            for(var9 = var4 - 1; var9 <= var4 + 1; ++var9) {
               var10 = CELL_2D[Hash2D(this.m_seed, var8, var9) & 255];
               var11 = (float)var8 - var1 + var10.x;
               var12 = (float)var9 - var2 + var10.y;
               var13 = Math.abs(var11) + Math.abs(var12);
               if (var13 < var5) {
                  var5 = var13;
                  var6 = var8;
                  var7 = var9;
               }
            }

            ++var8;
         }
      case 2:
         for(var8 = var3 - 1; var8 <= var3 + 1; ++var8) {
            for(var9 = var4 - 1; var9 <= var4 + 1; ++var9) {
               var10 = CELL_2D[Hash2D(this.m_seed, var8, var9) & 255];
               var11 = (float)var8 - var1 + var10.x;
               var12 = (float)var9 - var2 + var10.y;
               var13 = Math.abs(var11) + Math.abs(var12) + var11 * var11 + var12 * var12;
               if (var13 < var5) {
                  var5 = var13;
                  var6 = var8;
                  var7 = var9;
               }
            }
         }
      }

      switch(this.m_cellularReturnType.ordinal()) {
      case 0:
         return ValCoord2D(0, var6, var7);
      case 1:
         FastNoise.Float2 var14 = CELL_2D[Hash2D(this.m_seed, var6, var7) & 255];
         return this.m_cellularNoiseLookup.GetNoise((float)var6 + var14.x, (float)var7 + var14.y);
      case 2:
         return var5 - 1.0F;
      default:
         return 0.0F;
      }
   }

   private float SingleCellular(float x, float y, ProceduralStream<Double> sourceNoise, double iscale) {
      float var8;
      int var9;
      int var10;
      int var6 = FastRound(var1);
      int var7 = FastRound(var2);
      var8 = 999999.0F;
      var9 = 0;
      var10 = 0;
      int var11;
      int var12;
      FastNoise.Float2 var13;
      float var14;
      float var15;
      float var16;
      label76:
      switch(this.m_cellularDistanceFunction.ordinal()) {
      case 0:
      default:
         var11 = var6 - 1;

         while(true) {
            if (var11 > var6 + 1) {
               break label76;
            }

            for(var12 = var7 - 1; var12 <= var7 + 1; ++var12) {
               var13 = CELL_2D[Hash2D(this.m_seed, var11, var12) & 255];
               var14 = (float)var11 - var1 + var13.x;
               var15 = (float)var12 - var2 + var13.y;
               var16 = var14 * var14 + var15 * var15;
               if (var16 < var8) {
                  var8 = var16;
                  var9 = var11;
                  var10 = var12;
               }
            }

            ++var11;
         }
      case 1:
         var11 = var6 - 1;

         while(true) {
            if (var11 > var6 + 1) {
               break label76;
            }

            for(var12 = var7 - 1; var12 <= var7 + 1; ++var12) {
               var13 = CELL_2D[Hash2D(this.m_seed, var11, var12) & 255];
               var14 = (float)var11 - var1 + var13.x;
               var15 = (float)var12 - var2 + var13.y;
               var16 = Math.abs(var14) + Math.abs(var15);
               if (var16 < var8) {
                  var8 = var16;
                  var9 = var11;
                  var10 = var12;
               }
            }

            ++var11;
         }
      case 2:
         for(var11 = var6 - 1; var11 <= var6 + 1; ++var11) {
            for(var12 = var7 - 1; var12 <= var7 + 1; ++var12) {
               var13 = CELL_2D[Hash2D(this.m_seed, var11, var12) & 255];
               var14 = (float)var11 - var1 + var13.x;
               var15 = (float)var12 - var2 + var13.y;
               var16 = Math.abs(var14) + Math.abs(var15) + var14 * var14 + var15 * var15;
               if (var16 < var8) {
                  var8 = var16;
                  var9 = var11;
                  var10 = var12;
               }
            }
         }
      }

      switch(this.m_cellularReturnType.ordinal()) {
      case 0:
         return ((Double)var3.get((double)var9 * var4, (double)var10 * var4)).floatValue();
      case 1:
         FastNoise.Float2 var17 = CELL_2D[Hash2D(this.m_seed, var9, var10) & 255];
         return this.m_cellularNoiseLookup.GetNoise((float)var9 + var17.x, (float)var10 + var17.y);
      case 2:
         return var8 - 1.0F;
      default:
         return 0.0F;
      }
   }

   private float SingleCellular2Edge(float x, float y) {
      float var5;
      float var6;
      int var3 = FastRound(var1);
      int var4 = FastRound(var2);
      var5 = 999999.0F;
      var6 = 999999.0F;
      int var7;
      int var8;
      FastNoise.Float2 var9;
      float var10;
      float var11;
      float var12;
      label66:
      switch(this.m_cellularDistanceFunction.ordinal()) {
      case 0:
      default:
         var7 = var3 - 1;

         while(true) {
            if (var7 > var3 + 1) {
               break label66;
            }

            for(var8 = var4 - 1; var8 <= var4 + 1; ++var8) {
               var9 = CELL_2D[Hash2D(this.m_seed, var7, var8) & 255];
               var10 = (float)var7 - var1 + var9.x;
               var11 = (float)var8 - var2 + var9.y;
               var12 = var10 * var10 + var11 * var11;
               var6 = Math.max(Math.min(var6, var12), var5);
               var5 = Math.min(var5, var12);
            }

            ++var7;
         }
      case 1:
         var7 = var3 - 1;

         while(true) {
            if (var7 > var3 + 1) {
               break label66;
            }

            for(var8 = var4 - 1; var8 <= var4 + 1; ++var8) {
               var9 = CELL_2D[Hash2D(this.m_seed, var7, var8) & 255];
               var10 = (float)var7 - var1 + var9.x;
               var11 = (float)var8 - var2 + var9.y;
               var12 = Math.abs(var10) + Math.abs(var11);
               var6 = Math.max(Math.min(var6, var12), var5);
               var5 = Math.min(var5, var12);
            }

            ++var7;
         }
      case 2:
         for(var7 = var3 - 1; var7 <= var3 + 1; ++var7) {
            for(var8 = var4 - 1; var8 <= var4 + 1; ++var8) {
               var9 = CELL_2D[Hash2D(this.m_seed, var7, var8) & 255];
               var10 = (float)var7 - var1 + var9.x;
               var11 = (float)var8 - var2 + var9.y;
               var12 = Math.abs(var10) + Math.abs(var11) + var10 * var10 + var11 * var11;
               var6 = Math.max(Math.min(var6, var12), var5);
               var5 = Math.min(var5, var12);
            }
         }
      }

      switch(this.m_cellularReturnType.ordinal()) {
      case 3:
         return var6 - 1.0F;
      case 4:
         return var6 + var5 - 1.0F;
      case 5:
         return var6 - var5 - 1.0F;
      case 6:
         return var6 * var5 - 1.0F;
      case 7:
         return var5 / var6 - 1.0F;
      default:
         return 0.0F;
      }
   }

   public void GradientPerturb(Vector3f v3) {
      this.SingleGradientPerturb(this.m_seed, this.m_gradientPerturbAmp, this.m_frequency, var1);
   }

   public void GradientPerturbFractal(Vector3f v3) {
      int var2 = this.m_seed;
      float var3 = this.m_gradientPerturbAmp * this.m_fractalBounding;
      float var4 = this.m_frequency;
      this.SingleGradientPerturb(var2, var3, this.m_frequency, var1);

      for(int var5 = 1; var5 < this.m_octaves; ++var5) {
         var4 *= this.m_lacunarity;
         var3 *= this.m_gain;
         ++var2;
         this.SingleGradientPerturb(var2, var3, var4, var1);
      }

   }

   private void SingleGradientPerturb(int seed, float perturbAmp, float frequency, Vector3f v3) {
      float var5 = var4.x * var3;
      float var6 = var4.y * var3;
      float var7 = var4.z * var3;
      int var8 = FastFloor(var5);
      int var9 = FastFloor(var6);
      int var10 = FastFloor(var7);
      int var11 = var8 + 1;
      int var12 = var9 + 1;
      int var13 = var10 + 1;
      float var14;
      float var15;
      float var16;
      switch(this.m_interp.ordinal()) {
      case 0:
      default:
         var14 = var5 - (float)var8;
         var15 = var6 - (float)var9;
         var16 = var7 - (float)var10;
         break;
      case 1:
         var14 = InterpHermiteFunc(var5 - (float)var8);
         var15 = InterpHermiteFunc(var6 - (float)var9);
         var16 = InterpHermiteFunc(var7 - (float)var10);
         break;
      case 2:
         var14 = InterpQuinticFunc(var5 - (float)var8);
         var15 = InterpQuinticFunc(var6 - (float)var9);
         var16 = InterpQuinticFunc(var7 - (float)var10);
      }

      FastNoise.Float3 var17 = CELL_3D[Hash3D(var1, var8, var9, var10) & 255];
      FastNoise.Float3 var18 = CELL_3D[Hash3D(var1, var11, var9, var10) & 255];
      float var19 = Lerp(var17.x, var18.x, var14);
      float var20 = Lerp(var17.y, var18.y, var14);
      float var21 = Lerp(var17.z, var18.z, var14);
      var17 = CELL_3D[Hash3D(var1, var8, var12, var10) & 255];
      var18 = CELL_3D[Hash3D(var1, var11, var12, var10) & 255];
      float var22 = Lerp(var17.x, var18.x, var14);
      float var23 = Lerp(var17.y, var18.y, var14);
      float var24 = Lerp(var17.z, var18.z, var14);
      float var25 = Lerp(var19, var22, var15);
      float var26 = Lerp(var20, var23, var15);
      float var27 = Lerp(var21, var24, var15);
      var17 = CELL_3D[Hash3D(var1, var8, var9, var13) & 255];
      var18 = CELL_3D[Hash3D(var1, var11, var9, var13) & 255];
      var19 = Lerp(var17.x, var18.x, var14);
      var20 = Lerp(var17.y, var18.y, var14);
      var21 = Lerp(var17.z, var18.z, var14);
      var17 = CELL_3D[Hash3D(var1, var8, var12, var13) & 255];
      var18 = CELL_3D[Hash3D(var1, var11, var12, var13) & 255];
      var22 = Lerp(var17.x, var18.x, var14);
      var23 = Lerp(var17.y, var18.y, var14);
      var24 = Lerp(var17.z, var18.z, var14);
      var4.x += Lerp(var25, Lerp(var19, var22, var15), var16) * var2;
      var4.y += Lerp(var26, Lerp(var20, var23, var15), var16) * var2;
      var4.z += Lerp(var27, Lerp(var21, var24, var15), var16) * var2;
   }

   public void GradientPerturb(Vector2f v2) {
      this.SingleGradientPerturb(this.m_seed, this.m_gradientPerturbAmp, this.m_frequency, var1);
   }

   public void GradientPerturbFractal(Vector2f v2) {
      int var2 = this.m_seed;
      float var3 = this.m_gradientPerturbAmp * this.m_fractalBounding;
      float var4 = this.m_frequency;
      this.SingleGradientPerturb(var2, var3, this.m_frequency, var1);

      for(int var5 = 1; var5 < this.m_octaves; ++var5) {
         var4 *= this.m_lacunarity;
         var3 *= this.m_gain;
         ++var2;
         this.SingleGradientPerturb(var2, var3, var4, var1);
      }

   }

   private void SingleGradientPerturb(int seed, float perturbAmp, float frequency, Vector2f v2) {
      float var5 = var4.x * var3;
      float var6 = var4.y * var3;
      int var7 = FastFloor(var5);
      int var8 = FastFloor(var6);
      int var9 = var7 + 1;
      int var10 = var8 + 1;
      float var11;
      float var12;
      switch(this.m_interp.ordinal()) {
      case 0:
      default:
         var11 = var5 - (float)var7;
         var12 = var6 - (float)var8;
         break;
      case 1:
         var11 = InterpHermiteFunc(var5 - (float)var7);
         var12 = InterpHermiteFunc(var6 - (float)var8);
         break;
      case 2:
         var11 = InterpQuinticFunc(var5 - (float)var7);
         var12 = InterpQuinticFunc(var6 - (float)var8);
      }

      FastNoise.Float2 var13 = CELL_2D[Hash2D(var1, var7, var8) & 255];
      FastNoise.Float2 var14 = CELL_2D[Hash2D(var1, var9, var8) & 255];
      float var15 = Lerp(var13.x, var14.x, var11);
      float var16 = Lerp(var13.y, var14.y, var11);
      var13 = CELL_2D[Hash2D(var1, var7, var10) & 255];
      var14 = CELL_2D[Hash2D(var1, var9, var10) & 255];
      float var17 = Lerp(var13.x, var14.x, var11);
      float var18 = Lerp(var13.y, var14.y, var11);
      var4.x += Lerp(var15, var17, var12) * var2;
      var4.y += Lerp(var16, var18, var12) * var2;
   }

   public static enum Interp {
      Linear,
      Hermite,
      Quintic;

      // $FF: synthetic method
      private static FastNoise.Interp[] $values() {
         return new FastNoise.Interp[]{Linear, Hermite, Quintic};
      }
   }

   public static enum NoiseType {
      Value,
      ValueFractal,
      Perlin,
      PerlinFractal,
      Simplex,
      SimplexFractal,
      Cellular,
      WhiteNoise,
      Cubic,
      CubicFractal;

      // $FF: synthetic method
      private static FastNoise.NoiseType[] $values() {
         return new FastNoise.NoiseType[]{Value, ValueFractal, Perlin, PerlinFractal, Simplex, SimplexFractal, Cellular, WhiteNoise, Cubic, CubicFractal};
      }
   }

   public static enum FractalType {
      FBM,
      Billow,
      RigidMulti;

      // $FF: synthetic method
      private static FastNoise.FractalType[] $values() {
         return new FastNoise.FractalType[]{FBM, Billow, RigidMulti};
      }
   }

   public static enum CellularDistanceFunction {
      Euclidean,
      Manhattan,
      Natural;

      // $FF: synthetic method
      private static FastNoise.CellularDistanceFunction[] $values() {
         return new FastNoise.CellularDistanceFunction[]{Euclidean, Manhattan, Natural};
      }
   }

   public static enum CellularReturnType {
      CellValue,
      NoiseLookup,
      Distance,
      Distance2,
      Distance2Add,
      Distance2Sub,
      Distance2Mul,
      Distance2Div;

      // $FF: synthetic method
      private static FastNoise.CellularReturnType[] $values() {
         return new FastNoise.CellularReturnType[]{CellValue, NoiseLookup, Distance, Distance2, Distance2Add, Distance2Sub, Distance2Mul, Distance2Div};
      }
   }

   private static class Float2 {
      public final float x;
      public final float y;

      public Float2(float x, float y) {
         this.x = var1;
         this.y = var2;
      }
   }

   private static class Float3 {
      public final float x;
      public final float y;
      public final float z;

      public Float3(float x, float y, float z) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
      }
   }
}
