//
//  Play.swift
//  smasher
//
//  Created by admin on 23/04/2018.
//  Copyright © 2018 etudiant. All rights reserved.
//

import SpriteKit
import GameplayKit

class Play: GKState {
    unowned let scene: GameScene
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnter(from previousState: GKState?) {
        if previousState is Tap {
            let ball = scene.childNode(withName: "ball") as! SKSpriteNode
            ball.physicsBody!.applyImpulse(CGVector(dx: 15, dy: 15))
        }
    }
    
    func randomDirection() -> CGFloat {
        let speedFactor: CGFloat = 5
        if scene.randomFloat(from: 0, to: 100) >= 50 {
            return -speedFactor
        } else {
            return speedFactor
        }
    }
    
}

