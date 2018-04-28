//
//  MainMenu.swift
//  smasher
//
//  Created by admin on 23/04/2018.
//  Copyright © 2018 etudiant. All rights reserved.
//

import SpriteKit

class MainMenu: SKScene {
    var play = SKSpriteNode()
    
    override func didMove(to view: SKView) {
        play = self.childNode(withName: "play") as! SKSpriteNode
        }
    
    func loadGame() {
        let newScene = GameScene(fileNamed:"GameScene")
        newScene!.scaleMode = .aspectFit
        let reveal = SKTransition.flipHorizontal(withDuration: 0.5)
        self.view?.presentScene(newScene!, transition: reveal)

        
        
    }
    
    
    
}
